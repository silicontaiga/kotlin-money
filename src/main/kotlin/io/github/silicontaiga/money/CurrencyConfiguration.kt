package io.github.silicontaiga.money

/**
 * The currency changes an application declares inside [Money.configure].
 *
 * Adding and replacing are separate and symmetric calls rather than one combined one: a single
 * call would let a typo — `replaceCurrency("EURO", …)` — silently invent a currency instead of
 * failing.
 */
public class CurrencyConfiguration internal constructor(
    private val inForce: Map<String, Currency>,
) {
    private val changes = LinkedHashMap<String, Currency>()

    /**
     * Registers a currency the library does not already have under [code].
     *
     * A code need only be non-blank and unique — `MONOPOLY` and `1INCH` are as legal as `EUR` —
     * and is matched exactly, so `eur` and `EUR` are different codes.
     *
     * @throws IllegalArgumentException if [code] is blank, or a currency is already registered
     * under it.
     */
    public fun addCurrency(
        code: String,
        scale: Int,
        symbol: String,
    ) {
        require(code.isNotBlank()) { "A currency code must not be blank" }
        require(resolve(code) == null) {
            "A currency is already registered under the code $code; use replaceCurrency to change it"
        }
        changes[code] = Currency(code, scale, symbol)
    }

    /**
     * Replaces the definition already registered under [code].
     *
     * @throws IllegalArgumentException if no currency is registered under [code].
     */
    public fun replaceCurrency(
        code: String,
        scale: Int,
        symbol: String,
    ) {
        require(resolve(code) != null) {
            "No currency is registered under the code $code; use addCurrency to register one"
        }
        changes[code] = Currency(code, scale, symbol)
    }

    private fun resolve(code: String): Currency? = changes[code] ?: inForce[code]

    internal fun applyTo(base: Map<String, Currency>): Map<String, Currency> = base + changes
}
