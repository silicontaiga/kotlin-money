package io.github.silicontaiga.money.configuration

import io.github.silicontaiga.money.Currency
import io.github.silicontaiga.money.Money
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ReplaceAddedCurrencyTest : FunSpec({

    test("replaceCurrency accepts a code added earlier in the same block") {
        Money.configure {
            addCurrency(code = "MONOPOLY", scale = 0, symbol = "M")
            replaceCurrency(code = "MONOPOLY", scale = 2, symbol = "M")
        }

        Currency.of("MONOPOLY").scale shouldBe 2
    }
})
