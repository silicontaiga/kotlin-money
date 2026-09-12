package io.github.silicontaiga.money

import io.github.silicontaiga.percentage.percent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.RoundingMode

class MoneyPercentageTest : FunSpec({

    test("applying a percentage rounds to the money's scale, where the BigDecimal form is exact") {
        19.percent.of(12.34.eur) shouldBe 2.34.eur
    }

    test("applying a percentage rounds half to even") {
        19.percent.of(12.34.eur).amount shouldBe "2.34".toBigDecimal()
        50.percent.of(0.05.eur) shouldBe 0.02.eur
    }

    test("applying a percentage takes an explicit scale and rounding mode") {
        19.percent.of(12.34.eur, scale = 4) shouldBe "2.3446".money(EUR)
        50.percent.of(0.05.eur, scale = 2, roundingMode = RoundingMode.UP) shouldBe 0.03.eur
    }

    test("applying a percentage keeps the currency") {
        19.percent.of(12.34.usd).currency shouldBe Currency.of("USD")
    }

    test("increasedBy adds the percentage of the amount") {
        250.eur.increasedBy(19.percent) shouldBe 297.50.eur
    }

    test("decreasedBy subtracts the percentage of the amount") {
        250.eur.decreasedBy(19.percent) shouldBe 202.50.eur
    }

    test("asPercentageOf derives the proportion") {
        50.eur.asPercentageOf(250.eur) shouldBe 20.percent
    }

    test("asPercentageOf requires the same currency") {
        shouldThrow<MismatchedCurrencyException> { 50.eur.asPercentageOf(250.usd) }
    }

    test("asPercentageOf a zero amount throws") {
        shouldThrow<ArithmeticException> { 50.eur.asPercentageOf(0.eur) }
    }
})

private val EUR = Currency.of("EUR")
