package io.github.silicontaiga.money

import java.math.BigDecimal

/**
 * This number denominated in [currency], so `123.money(Monopoly)` is `123.00 MONOPOLY`.
 *
 * The literal spelling of [Money.of], and the form to reach for with any currency that has no
 * shipped suffix of its own.
 */
public fun Int.money(currency: Currency): Money = Money.of(this, currency)

/** This number denominated in [currency]. The literal spelling of [Money.of]. */
public fun Long.money(currency: Currency): Money = Money.of(this, currency)

/**
 * This number denominated in [currency]. The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public fun Double.money(currency: Currency): Money = Money.of(this, currency)

/**
 * This text denominated in [currency], keeping every place it carries.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public fun String.money(currency: Currency): Money = Money.of(this, currency)

/** This number denominated in [currency]. The literal spelling of [Money.of]. */
public fun BigDecimal.money(currency: Currency): Money = Money.of(this, currency)
