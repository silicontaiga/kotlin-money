package io.github.silicontaiga.money.configuration

import io.github.silicontaiga.money.Money
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain

class AddCurrencyRejectionTest : FunSpec({

    test("addCurrency refuses a code that already exists, rather than replacing it") {
        val thrown =
            shouldThrow<IllegalArgumentException> {
                Money.configure { addCurrency(code = "EUR", scale = 4, symbol = "€") }
            }
        thrown.message shouldContain "EUR"
    }

    test("addCurrency refuses a blank code") {
        shouldThrow<IllegalArgumentException> {
            Money.configure { addCurrency(code = "  ", scale = 2, symbol = "?") }
        }
    }

    test("addCurrency refuses a code it has already added in the same block") {
        shouldThrow<IllegalArgumentException> {
            Money.configure {
                addCurrency(code = "MONOPOLY", scale = 0, symbol = "M")
                addCurrency(code = "MONOPOLY", scale = 2, symbol = "M")
            }
        }
    }
})
