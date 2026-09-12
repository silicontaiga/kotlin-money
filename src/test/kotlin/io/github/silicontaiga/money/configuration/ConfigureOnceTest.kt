package io.github.silicontaiga.money.configuration

import io.github.silicontaiga.money.Money
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec

class ConfigureOnceTest : FunSpec({

    test("a second configure call throws, because the definition set is write-once") {
        Money.configure { addCurrency(code = "MONOPOLY", scale = 0, symbol = "M") }

        shouldThrow<IllegalStateException> {
            Money.configure { addCurrency(code = "ARCADE", scale = 0, symbol = "A") }
        }
    }
})
