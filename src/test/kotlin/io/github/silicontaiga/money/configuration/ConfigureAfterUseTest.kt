package io.github.silicontaiga.money.configuration

import io.github.silicontaiga.money.Money
import io.github.silicontaiga.money.eur
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec

class ConfigureAfterUseTest : FunSpec({

    test("configuring after a money exists throws") {
        1.eur

        shouldThrow<IllegalStateException> {
            Money.configure { addCurrency(code = "MONOPOLY", scale = 0, symbol = "M") }
        }
    }
})
