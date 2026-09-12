package io.github.silicontaiga.money

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.RoundingMode

class MoneyHelpersTest : FunSpec({

    test("isZero holds whatever the scale") {
        0.eur.isZero shouldBe true
        0.eur(4).isZero shouldBe true
        1.eur.isZero shouldBe false
    }

    test("isPositive excludes zero") {
        1.eur.isPositive shouldBe true
        0.eur.isPositive shouldBe false
        (-1).eur.isPositive shouldBe false
    }

    test("isNegative excludes zero") {
        (-1).eur.isNegative shouldBe true
        0.eur.isNegative shouldBe false
        1.eur.isNegative shouldBe false
    }

    test("withScale brings a money to the scale asked for") {
        123.eur.withScale(4).amount shouldBe "123.0000".toBigDecimal()
    }

    test("withScale rounds half to even by default") {
        0.125.eur(3).withScale(2) shouldBe 0.12.eur
    }

    test("withScale takes an explicit rounding mode") {
        0.125.eur(3).withScale(2, RoundingMode.UP) shouldBe 0.13.eur
    }

    test("withCurrencyScale reaches a payable amount") {
        "0.033".money(Currency.of("EUR")).withCurrencyScale() shouldBe 0.03.eur
    }

    test("withCurrencyScale takes an explicit rounding mode") {
        "0.033".money(Currency.of("EUR")).withCurrencyScale(RoundingMode.UP) shouldBe 0.04.eur
    }

    test("withCurrencyScale pads a money quoted more coarsely than its currency") {
        123.eur(0).withCurrencyScale().amount shouldBe "123.00".toBigDecimal()
    }
})
