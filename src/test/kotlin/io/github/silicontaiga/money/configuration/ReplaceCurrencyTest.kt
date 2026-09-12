package io.github.silicontaiga.money.configuration

import io.github.silicontaiga.money.Currencies
import io.github.silicontaiga.money.Currency
import io.github.silicontaiga.money.Money
import io.github.silicontaiga.money.eur
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe

class ReplaceCurrencyTest : FunSpec({

    beforeSpec {
        Money.configure {
            replaceCurrency(code = "EUR", scale = 4, symbol = "€")
        }
    }

    test("replaceCurrency changes a shipped definition") {
        Currency.of("EUR").scale shouldBe 4
    }

    test("the shipped views show a replaced currency in its replaced form") {
        Currencies.iso shouldContain Currency.of("EUR")
        Currencies.iso.single { it.code == "EUR" }.scale shouldBe 4
    }

    test("a shipped literal resolves the replaced definition, not the original") {
        123.eur.amount shouldBe "123.0000".toBigDecimal()
    }
})
