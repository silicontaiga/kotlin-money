package io.github.silicontaiga.money

/**
 * The currency definitions in force in this JVM.
 *
 * Holds exactly one [Currency] object per code, which is what lets identity and equality coincide.
 * The set is write-once: it may be reconfigured before the first [Money] is built, and never again.
 */
internal object CurrencyRegistry {
    private var currencies: Map<String, Currency> = build(ShippedCurrencies.all)
    private var configured = false
    private var used = false

    private fun build(definitions: List<CurrencyDefinition>): Map<String, Currency> =
        definitions.associate { it.code to Currency(it.code, it.scale, it.symbol) }

    fun find(code: String): Currency? = currencies[code]

    /** Records that a [Money] now exists, which closes the set to further configuration. */
    fun markUsed() {
        used = true
    }

    fun configure(block: CurrencyConfiguration.() -> Unit) {
        check(!configured) { "Currencies have already been configured; the definition set is write-once" }
        check(!used) { "Currencies cannot be configured once a Money exists; configure before any money is built" }
        val configuration = CurrencyConfiguration(currencies)
        configuration.block()
        currencies = configuration.applyTo(currencies)
        configured = true
    }
}
