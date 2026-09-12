package io.github.silicontaiga.money.configuration

import io.github.silicontaiga.money.Currencies
import io.github.silicontaiga.money.Currency
import io.github.silicontaiga.money.Money
import io.github.silicontaiga.money.money
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe

class AddCurrencyTest : FunSpec({

    test("addCurrency registers a currency the library does not ship") {
        Money.configure {
            addCurrency(code = "MONOPOLY", scale = 0, symbol = "M")
        }

        val monopoly = Currency.of("MONOPOLY")
        monopoly.scale shouldBe 0
        monopoly.symbol shouldBe "M"
        123.money(monopoly).amount shouldBe "123".toBigDecimal()
        Currencies.iso shouldNotContain monopoly
        Currencies.crypto shouldNotContain monopoly
    }
})
