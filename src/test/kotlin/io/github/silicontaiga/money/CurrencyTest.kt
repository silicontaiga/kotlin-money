package io.github.silicontaiga.money

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeSameInstanceAs

class CurrencyTest : FunSpec({

    test("of resolves a shipped currency by its code") {
        Currency.of("EUR").code shouldBe "EUR"
    }

    test("of carries the currency's scale") {
        Currency.of("EUR").scale shouldBe 2
    }

    test("of carries a zero scale for a currency with no minor unit") {
        Currency.of("JPY").scale shouldBe 0
    }

    test("of carries the currency's symbol") {
        Currency.of("EUR").symbol shouldBe "€"
    }

    test("of rejects an unknown code") {
        shouldThrow<IllegalArgumentException> { Currency.of("XYZ") }
    }

    test("of names the unknown code it rejected") {
        val thrown = shouldThrow<IllegalArgumentException> { Currency.of("XYZ") }
        thrown.message shouldContain "XYZ"
    }

    test("ofOrNull resolves a shipped currency") {
        Currency.ofOrNull("EUR")?.code shouldBe "EUR"
    }

    test("ofOrNull returns null for an unknown code") {
        Currency.ofOrNull("XYZ") shouldBe null
    }

    test("a code resolves to one object, so identity and equality coincide") {
        Currency.of("EUR") shouldBeSameInstanceAs Currency.of("EUR")
    }

    test("codes are compared exactly, with no case folding") {
        Currency.ofOrNull("eur") shouldBe null
    }

    test("of carries a three-place scale for the Kuwaiti dinar") {
        Currency.of("KWD").scale shouldBe 3
    }

    test("of carries a four-place scale for the Chilean unit of account") {
        Currency.of("CLF").scale shouldBe 4
    }

    test("of resolves a cryptocurrency") {
        Currency.of("BTC").scale shouldBe 8
    }

    test("of carries an eighteen-place scale for ether") {
        Currency.of("ETH").scale shouldBe 18
    }

    test("symbol falls back to the code for a currency with no distinctive one") {
        Currency.of("CHF").symbol shouldBe "CHF"
    }

    test("of resolves a withdrawn historical currency") {
        Currency.of("DEM").scale shouldBe 2
    }
})
