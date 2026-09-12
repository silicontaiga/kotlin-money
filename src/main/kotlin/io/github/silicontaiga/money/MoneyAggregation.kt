package io.github.silicontaiga.money

/**
 * Returns the sum of these amounts, so `listOf(3.34.eur, 3.33.eur).sum()` is `6.67 EUR`.
 *
 * Exact, and the result takes the largest scale among the elements, consistent with [Money.plus].
 *
 * @throws IllegalArgumentException if there are no amounts. Unlike a sum of plain numbers this has
 * no zero to fall back on, because a zero has to be denominated in something — reach for
 * [Money.total] instead, which names the currency.
 * @throws MismatchedCurrencyException if the amounts are not all in one currency.
 */
public fun Iterable<Money>.sum(): Money {
    val amounts = iterator()
    require(amounts.hasNext()) {
        "Cannot sum no money: there is no currency to return a zero in. Use Money.total(currency, amounts) instead"
    }
    var running = amounts.next()
    while (amounts.hasNext()) {
        running += amounts.next()
    }
    return running
}
