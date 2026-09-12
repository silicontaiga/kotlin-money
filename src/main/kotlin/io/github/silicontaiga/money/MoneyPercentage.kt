package io.github.silicontaiga.money

import io.github.silicontaiga.percentage.Percentage
import io.github.silicontaiga.percentage.asPercentageOf
import io.github.silicontaiga.percentage.decreasedBy
import io.github.silicontaiga.percentage.increasedBy
import java.math.RoundingMode

/**
 * Returns this percentage of [money], so `19.percent.of(12.34.eur)` is `2.34 EUR`.
 *
 * **This rounds, where `Percentage.of(BigDecimal)` is exact and offers no rounding parameter at
 * all.** Same name, different contract: 19% of `12.34 EUR` is `2.3446`, and a money has to land on
 * a scale. The result takes [money]'s own scale, half to even; the overload below says otherwise.
 */
public fun Percentage.of(money: Money): Money = of(money, money.amount.scale())

/**
 * Returns this percentage of [money] at exactly [scale] decimal places.
 *
 * @throws ArithmeticException if [roundingMode] is [RoundingMode.UNNECESSARY] and the exact result
 * does not fit [scale].
 */
public fun Percentage.of(
    money: Money,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
): Money = Money(of(money.amount).setScale(scale, roundingMode), money.currency)

/**
 * Returns this money increased by [percentage], so `250.eur.increasedBy(19.percent)` is
 * `297.50 EUR`.
 *
 * Rounds to this money's scale, half to even.
 */
public fun Money.increasedBy(percentage: Percentage): Money = increasedBy(percentage, amount.scale())

/** Returns this money increased by [percentage], at exactly [scale] decimal places. */
public fun Money.increasedBy(
    percentage: Percentage,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
): Money = Money(amount.increasedBy(percentage).setScale(scale, roundingMode), currency)

/**
 * Returns this money decreased by [percentage], so `250.eur.decreasedBy(19.percent)` is
 * `202.50 EUR`.
 *
 * Rounds to this money's scale, half to even.
 */
public fun Money.decreasedBy(percentage: Percentage): Money = decreasedBy(percentage, amount.scale())

/** Returns this money decreased by [percentage], at exactly [scale] decimal places. */
public fun Money.decreasedBy(
    percentage: Percentage,
    scale: Int,
    roundingMode: RoundingMode = RoundingMode.HALF_EVEN,
): Money = Money(amount.decreasedBy(percentage).setScale(scale, roundingMode), currency)

/**
 * Returns what proportion this money is of [base], so `50.eur.asPercentageOf(250.eur)` is `20%`.
 *
 * A percentage rather than a money: a ratio of two amounts in one currency cancels the currency.
 *
 * @throws MismatchedCurrencyException if [base] is in a different currency.
 * @throws ArithmeticException if [base] is zero.
 */
public fun Money.asPercentageOf(base: Money): Percentage {
    requireSameCurrency(base)
    return amount.asPercentageOf(base.amount)
}
