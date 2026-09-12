package io.github.silicontaiga.money

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

class CurrencyConfigurationTest : FunSpec({

    test("iso includes a circulating fiat currency") {
        Currencies.iso shouldContain Currency.of("EUR")
    }

    test("iso includes a withdrawn historical currency") {
        Currencies.iso shouldContain Currency.of("DEM")
    }

    test("iso excludes cryptocurrencies") {
        Currencies.iso shouldNotContain Currency.of("BTC")
    }

    test("crypto includes bitcoin") {
        Currencies.crypto shouldContain Currency.of("BTC")
    }

    test("crypto excludes ISO currencies") {
        Currencies.crypto shouldNotContain Currency.of("EUR")
    }

    test("the shipped sets are disjoint") {
        Currencies.iso.intersect(Currencies.crypto.toSet()) shouldBe emptySet()
    }

    test("every shipped currency resolves under its own code") {
        val shipped = Currencies.iso + Currencies.crypto
        shipped.forAll { Currency.of(it.code) shouldBe it }
    }
})
