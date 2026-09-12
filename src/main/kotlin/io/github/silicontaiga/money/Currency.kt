package io.github.silicontaiga.money

/**
 * The denomination a monetary amount is measured in, identified by its [code].
 *
 * A code identifies exactly one currency, so there is exactly one `Currency` object per code and
 * identity and equality coincide. Instances are obtained from [Currency.of] and [Currency.ofOrNull]
 * — the constructor is not public, and the class cannot be subclassed, so no second currency can
 * exist for a code that is already in force.
 *
 * The set is deliberately open: circulating fiat currencies, withdrawn historical ones,
 * cryptocurrencies and private currencies such as play money all qualify, and an application may
 * register its own through [Money.configure].
 */
public class Currency internal constructor(
    /**
     * This currency's identifier, such as `EUR`.
     *
     * For real-world currencies this is the ISO 4217 alphabetic code, but that is a property of
     * those currencies rather than a rule: a code need only be non-blank and unique. Codes are
     * compared exactly, so `eur` and `EUR` are different codes.
     */
    public val code: String,
    /**
     * How many decimal places this currency is normally quoted at: `2` for the euro, `0` for the
     * yen, `3` for the Kuwaiti dinar, `8` for bitcoin, `18` for ether.
     *
     * The same number ISO 4217 expresses as the currency's minor unit. It is a floor on the
     * precision a [Money] is held at, never a cap.
     */
    public val scale: Int,
    /**
     * The short label this currency is written with, such as `€`.
     *
     * A label only, and not a formatting facility: symbols are shared between currencies — `$`
     * belongs to the US, Canadian, Australian, Singapore and Hong Kong dollars alike — so a symbol
     * never says which currency an amount is in. Falls back to the [code] for the many currencies
     * that have no distinctive symbol.
     */
    public val symbol: String,
) {
    /** Ways of obtaining a [Currency]. */
    public companion object {
        /**
         * Returns the currency registered under [code].
         *
         * @throws IllegalArgumentException if no currency is registered under [code].
         */
        public fun of(code: String): Currency =
            requireNotNull(CurrencyRegistry.find(code)) { "No currency is registered under the code $code" }

        /**
         * Returns the currency registered under [code], or `null` if there is none.
         *
         * The form to reach for at a boundary parsing untrusted input, such as a currency code read
         * from a database column or a JSON field.
         */
        public fun ofOrNull(code: String): Currency? = CurrencyRegistry.find(code)
    }
}
