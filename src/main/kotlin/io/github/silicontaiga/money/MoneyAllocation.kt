package io.github.silicontaiga.money

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

/**
 * Splits this money into [parts] equal pieces that sum to **exactly** this money.
 *
 * Parts are computed at [scale], this money's own by default. The whole units left over after an
 * even division are handed out one at a time according to [bias], so `10.eur` split three ways is
 * `3.34`, `3.33`, `3.33` rather than three rounded thirds.
 *
 * Whatever remains *below* [scale] goes to the part the [bias] starts from, which therefore ends up
 * with a finer scale than its siblings: `62.56.eur.allocate(3, scale = 0)` is `21.56`, `21`, `20`.
 * **The returned parts are consequently not uniform in scale**, and since scale is part of equality
 * that is visible to callers. A caller who needs uniform parts should choose a scale the amount is
 * representable at, or round the money first.
 *
 * Negative amounts mirror the positive case, so `(-m).allocate(n)` equals
 * `m.allocate(n).map { -it }` and a credit note still matches the invoice it reverses. That works
 * because the split is computed on the magnitude and the sign re-applied: dividing a negative
 * amount toward zero instead would lose the remainder outright.
 *
 * @throws IllegalArgumentException if [parts] is less than one.
 */
public fun Money.allocate(
    parts: Int,
    scale: Int = amount.scale(),
    bias: RemainderBias = RemainderBias.FIRST,
): List<Money> {
    require(parts >= 1) { "Cannot split money into $parts parts; at least one is needed" }
    return allocateByRatios(List(parts) { 1 }, scale, bias)
}

/**
 * Splits this money in proportion to [ratios], into pieces summing to **exactly** this money.
 *
 * `10.eur.allocate(listOf(1, 1, 3))` is `2.00`, `2.00`, `6.00`. Scale, bias and the treatment of
 * leftovers are as described on the equal-split [allocate].
 *
 * The ratios arrive as a list rather than as a `vararg` deliberately. A `vararg` of `Int` would
 * collide with the equal-split overload at two positional arguments: Kotlin resolves
 * `allocate(1, 1)` to `parts = 1, scale = 1`, so a caller asking for a 50/50 split would silently
 * receive one undivided part. Differing by parameter type instead puts resolution beyond doubt.
 *
 * A ratio may be zero — a party entitled to none of this particular split — and such a part is
 * skipped when the leftovers are handed out, since a share of nothing stays nothing.
 *
 * @throws IllegalArgumentException if [ratios] is empty, if any ratio is negative, or if they sum
 * to zero, there being no proportion to divide by.
 */
public fun Money.allocate(
    ratios: List<Int>,
    scale: Int = amount.scale(),
    bias: RemainderBias = RemainderBias.FIRST,
): List<Money> = allocateByRatios(ratios, scale, bias)

private fun Money.allocateByRatios(
    ratios: List<Int>,
    scale: Int,
    bias: RemainderBias,
): List<Money> {
    require(ratios.isNotEmpty()) { "Cannot split money with no ratios" }
    require(ratios.none { it < 0 }) { "A ratio must not be negative, but $ratios was given" }
    val ratioTotal = ratios.sumOf { it.toLong() }
    require(ratioTotal > 0) { "Ratios must not sum to zero, but $ratios does" }

    // Work in whole units of 10^-scale, so the division is exact integer arithmetic. Anything the
    // amount carries below that scale is held back and handed to the biased part at the end.
    val magnitude = amount.abs()
    val truncated = magnitude.setScale(scale, RoundingMode.DOWN)
    val subScaleRemainder = magnitude - truncated
    val totalUnits = truncated.unscaledValue()
    val divisor = ratioTotal.toBigInteger()

    val base = ratios.map { totalUnits * it.toBigInteger() / divisor }
    val leftover = (totalUnits - base.fold(BigInteger.ZERO, BigInteger::add)).toInt()

    val order =
        when (bias) {
            RemainderBias.FIRST -> ratios.indices.toList()
            RemainderBias.LAST -> ratios.indices.reversed().toList()
        }
    val entitled = order.filter { ratios[it] > 0 }
    val receivesUnit = entitled.take(leftover).toSet()
    val receivesRemainder = entitled.first()

    val hasRemainder = subScaleRemainder.signum() != 0
    val negative = amount.signum() < 0
    return base.mapIndexed { index, units ->
        val whole = if (index in receivesUnit) units + BigInteger.ONE else units
        val value = BigDecimal(whole, scale)
        // Only a remainder that actually exists is handed over. Adding a zero one would still
        // raise the part's scale, and scale is part of equality.
        val part = if (index == receivesRemainder && hasRemainder) value + subScaleRemainder else value
        Money(if (negative) part.negate() else part, currency)
    }
}
