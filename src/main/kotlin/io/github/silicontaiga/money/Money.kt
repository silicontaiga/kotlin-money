package io.github.silicontaiga.money

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/** Returns this value at [scale] decimal places, or unchanged if it already has more. */
private fun BigDecimal.atLeastScale(scale: Int): BigDecimal = if (this.scale() < scale) setScale(scale) else this

/**
 * Returns this number as the decimal it was written as.
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite, neither of which has a decimal
 * expansion to convert.
 */
private fun Double.toExactBigDecimal(): BigDecimal {
    require(isFinite()) { "Cannot represent $this as a decimal amount" }
    return BigDecimal.valueOf(this)
}

/**
 * An amount of value denominated in exactly one [currency].
 *
 * Money values are immutable, and negative amounts are legal — ledgers need credits as well as
 * debits. A money keeps the scale it was given: see [amount].
 */
public class Money internal constructor(
    /**
     * The signed numeric quantity of this money, at the scale it was given.
     *
     * The scale is part of what the value says, so trailing zeros are never stripped: `123.eur`
     * carries `123.00` and `123.eur(4)` carries `123.0000`.
     */
    public val amount: BigDecimal,
    /** The denomination this amount is measured in. */
    public val currency: Currency,
) : Comparable<Money> {
    init {
        // A money now exists, which closes the currency set to further configuration.
        CurrencyRegistry.markUsed()
    }

    /**
     * Returns this money at exactly [scale] decimal places, so `123.eur(4)` is `123.0000 EUR`.
     *
     * This is what makes a scale nameable on a literal. A scale finer than the value needs pads
     * it; a coarser one **rounds**, by [roundingMode].
     */
    public operator fun invoke(
        scale: Int,
        roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
    ): Money = Money(amount.setScale(scale, roundingMode), currency)

    /**
     * Returns the sum of this money and [other], so `12.34.eur + 0.66.eur` is `13.00 EUR`.
     *
     * **Exact.** Nothing is rounded, and the result takes the larger of the two operand scales, so
     * `123.eur(4) + 123.eur(6)` has scale 6.
     *
     * @throws MismatchedCurrencyException if [other] is in a different currency.
     */
    public operator fun plus(other: Money): Money {
        requireSameCurrency(other)
        return Money(amount + other.amount, currency)
    }

    /**
     * Returns the difference of this money and [other], so `13.eur - 0.66.eur` is `12.34 EUR`.
     *
     * **Exact**, on the same terms as [plus]. The result may be negative, which is legal.
     *
     * @throws MismatchedCurrencyException if [other] is in a different currency.
     */
    public operator fun minus(other: Money): Money {
        requireSameCurrency(other)
        return Money(amount - other.amount, currency)
    }

    /**
     * Returns this money scaled by [factor], so `12.34.eur * 3` is `37.02 EUR`.
     *
     * **Rounds** to this money's own scale, half to even. Use the overload taking a scale and a
     * rounding mode to say otherwise.
     */
    public operator fun times(factor: Int): Money = times(factor.toBigDecimal())

    /** Returns this money scaled by [factor]. Rounds to this money's scale, half to even. */
    public operator fun times(factor: Long): Money = times(factor.toBigDecimal())

    /**
     * Returns this money scaled by [factor]. Rounds to this money's scale, half to even.
     *
     * @throws IllegalArgumentException if [factor] is `NaN` or infinite.
     */
    public operator fun times(factor: Double): Money = times(factor.toExactBigDecimal())

    /** Returns this money scaled by [factor]. Rounds to this money's scale, half to even. */
    public operator fun times(factor: BigDecimal): Money = times(factor, amount.scale())

    /** Returns this money scaled by [factor], at exactly [scale] decimal places. */
    public fun times(
        factor: BigDecimal,
        scale: Int,
        roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
    ): Money = Money((amount * factor).setScale(scale, roundingMode), currency)

    /**
     * Returns this money divided by [divisor], so `10.eur / 3` is `3.33 EUR`.
     *
     * **Rounds to this money's scale, discarding what does not fit** — the remaining cent of
     * `10.eur / 3` is lost. That is the difference between division and allocation: this scales a
     * single amount down, while [allocate] splits an amount among parties with nothing lost.
     * Anyone dividing money between people wants `allocate`.
     *
     * @throws ArithmeticException if [divisor] is zero.
     */
    public operator fun div(divisor: Int): Money = div(divisor.toBigDecimal())

    /** Returns this money divided by [divisor]. Rounds to this money's scale, half to even. */
    public operator fun div(divisor: Long): Money = div(divisor.toBigDecimal())

    /**
     * Returns this money divided by [divisor]. Rounds to this money's scale, half to even.
     *
     * @throws IllegalArgumentException if [divisor] is `NaN` or infinite.
     * @throws ArithmeticException if [divisor] is zero.
     */
    public operator fun div(divisor: Double): Money = div(divisor.toExactBigDecimal())

    /** Returns this money divided by [divisor]. Rounds to this money's scale, half to even. */
    public operator fun div(divisor: BigDecimal): Money = div(divisor, amount.scale())

    /** Returns this money divided by [divisor], at exactly [scale] decimal places. */
    public fun div(
        divisor: BigDecimal,
        scale: Int,
        roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
    ): Money = Money(amount.divide(divisor, scale, roundingMode), currency)

    /**
     * Returns how many times [other] fits into this money, so `10.eur / 4.eur` is `2.5`.
     *
     * A plain number rather than a money: dividing an amount by an amount cancels the currency.
     * It is a genuine division, so it rounds — to 34 significant digits, half to even
     * ([MathContext.DECIMAL128]) — and an overload takes an explicit context.
     *
     * @throws MismatchedCurrencyException if [other] is in a different currency.
     * @throws ArithmeticException if [other] is zero; there is no `NaN` in exact decimal arithmetic.
     */
    public operator fun div(other: Money): BigDecimal = div(other, MathContext.DECIMAL128)

    /** Returns how many times [other] fits into this money, rounding to [context]. */
    public fun div(
        other: Money,
        context: MathContext,
    ): BigDecimal {
        requireSameCurrency(other)
        return amount.divide(other.amount, context)
    }

    /** Returns this money with its sign flipped, so `-(2.eur)` is `-2.00 EUR`. Exact. */
    public operator fun unaryMinus(): Money = Money(amount.negate(), currency)

    /** Returns this money with any negative sign removed, so `(-2).eur.abs()` is `2.00 EUR`. Exact. */
    public fun abs(): Money = Money(amount.abs(), currency)

    /**
     * Compares this money with [other] **by value alone**, ignoring scale, so `10.eur(2)` and
     * `10.eur(4)` compare as equal even though they are not `==`.
     *
     * Ordering across currencies has no correct answer, so it throws rather than inventing one —
     * which means a mixed-currency list cannot be sorted. Ordering by code and then by amount
     * would have given a total order at the price of `10.eur < 5.usd` being `true`, and a
     * confident wrong answer about money is worse than a thrown exception.
     *
     * @throws MismatchedCurrencyException if [other] is in a different currency.
     */
    override fun compareTo(other: Money): Int {
        requireSameCurrency(other)
        return amount.compareTo(other.amount)
    }

    private fun requireSameCurrency(other: Money) {
        if (currency != other.currency) throw MismatchedCurrencyException(currency, other.currency)
    }

    /**
     * Returns whether [other] is a money with the same amount, the same scale and the same
     * currency.
     *
     * Scale is part of what a money is, so `10.eur(2)` does not equal `10.eur(4)`. Use
     * [hasSameValueAs] to compare amounts whatever their scale.
     *
     * **This makes `equals` inconsistent with [compareTo]**, exactly as `BigDecimal`'s is: a
     * `HashSet` holding `10.eur(2)` and `10.eur(4)` has two entries where a `TreeSet` has one.
     */
    override fun equals(other: Any?): Boolean = this === other || (other is Money && amount == other.amount && currency == other.currency)

    /**
     * Returns whether [other] holds the same amount in the same currency, whatever its scale.
     *
     * The scale-blind companion to [equals]: `10.eur(2).hasSameValueAs(10.eur(4))` is `true` where
     * `==` is `false`. Two moneys in different currencies never have the same value, so this
     * returns `false` rather than throwing — it compares values instead of combining them.
     */
    public fun hasSameValueAs(other: Money): Boolean = currency == other.currency && amount.compareTo(other.amount) == 0

    /** Returns a hash code consistent with [equals], and so sensitive to scale. */
    override fun hashCode(): Int = 31 * amount.hashCode() + currency.hashCode()

    /** Ways of obtaining a [Money]. */
    public companion object {
        /**
         * Extends or replaces the shipped currency definitions, **once, before any money exists**.
         *
         * ```kotlin
         * Money.configure {
         *     replaceCurrency(code = "EUR", scale = 4, symbol = "€")
         *     addCurrency(code = "MONOPOLY", scale = 0, symbol = "M")
         * }
         * ```
         *
         * **This is an application-level facility, and a library must never call it**: reconfiguring
         * currencies silently changes the meaning of its consumer's money. Tests that use it affect
         * the whole JVM and need isolating accordingly.
         *
         * @throws IllegalStateException if currencies have already been configured, or a [Money] has
         * already been constructed.
         */
        public fun configure(block: CurrencyConfiguration.() -> Unit): Unit = CurrencyRegistry.configure(block)

        /**
         * Returns [amount] denominated in [currency], at the currency's scale.
         *
         * The currency's scale is a floor and never a cap, so a value needing more decimal places
         * than the currency keeps them.
         */
        public fun of(
            amount: Int,
            currency: Currency,
        ): Money = of(amount.toBigDecimal(), currency)

        /**
         * Returns [amount] denominated in [currency], keeping the places the value already has.
         *
         * The currency's scale is a floor and never a cap: a value quoted more coarsely is raised
         * to it, and one that needs more decimal places keeps them.
         */
        public fun of(
            amount: BigDecimal,
            currency: Currency,
        ): Money = Money(amount.atLeastScale(currency.scale), currency)

        /**
         * Returns [amount] denominated in [currency] at exactly [scale] decimal places.
         *
         * Naming a scale finer than the value needs pads it; naming one coarser **rounds**, by
         * [roundingMode], which is what distinguishes a named scale from the currency's own.
         */
        public fun of(
            amount: BigDecimal,
            currency: Currency,
            scale: Int,
            roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
        ): Money = Money(amount.setScale(scale, roundingMode), currency)

        /** Returns [amount] denominated in [currency], at the currency's scale. */
        public fun of(
            amount: Long,
            currency: Currency,
        ): Money = of(amount.toBigDecimal(), currency)

        /**
         * Returns [amount] denominated in [currency], at the currency's scale.
         *
         * The value is read as the decimal it was written as, not as its binary expansion, so
         * `0.1` means a tenth. Note that a `Double` literal cannot carry trailing zeros at all —
         * `123.1200` is already `123.12` before any library code runs — and carries about 17
         * significant digits, so a `String` is the literal to reach for when neither is acceptable.
         *
         * @throws IllegalArgumentException if [amount] is `NaN` or infinite.
         */
        public fun of(
            amount: Double,
            currency: Currency,
        ): Money = of(amount.toExactBigDecimal(), currency)

        /**
         * Returns [amount] denominated in [currency], keeping every place the text carries.
         *
         * The exact-entry door: a `String` is the only literal that can express both trailing
         * zeros and a value too long for a `Double` to hold, such as an 18-decimal token amount.
         *
         * @throws NumberFormatException if [amount] is not a valid decimal number.
         */
        public fun of(
            amount: String,
            currency: Currency,
        ): Money = of(BigDecimal(amount), currency)

        /** Returns [amount] denominated in [currency] at exactly [scale] decimal places. */
        public fun of(
            amount: Int,
            currency: Currency,
            scale: Int,
            roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
        ): Money = of(amount.toBigDecimal(), currency, scale, roundingMode)
    }
}
