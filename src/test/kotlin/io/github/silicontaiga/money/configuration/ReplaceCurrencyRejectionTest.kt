package io.github.silicontaiga.money.configuration

import io.github.silicontaiga.money.Money
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain

class ReplaceCurrencyRejectionTest : FunSpec({

    test("replaceCurrency refuses an unknown code, so a typo cannot invent a currency") {
        val thrown =
            shouldThrow<IllegalArgumentException> {
                Money.configure { replaceCurrency(code = "EURO", scale = 4, symbol = "€") }
            }
        thrown.message shouldContain "EURO"
    }
})
