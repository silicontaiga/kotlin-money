package io.github.silicontaiga.money

import io.github.silicontaiga.percentage.percent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.BigDecimal
import java.math.RoundingMode

/*
 * Every worked example in README.md, compiled and asserted.
 *
 * The README shipped three examples that did not compile — two naming a bare `EUR` constant that
 * does not exist, and one using an identifier defined 140 lines further down. Nothing in the build
 * noticed, because documentation is not code. This spec makes it code: an example that stops
 * compiling, or whose stated output stops being true, fails here.
 *
 * Keep it in step with README.md. If an example changes there, change it here.
 */
val Int.thb: Money get() = Money.of(this, Currency.of("THB"))
val String.thb: Money get() = Money.of(this, Currency.of("THB"))
val Int.`try`: Money get() = Money.of(this, Currency.of("TRY"))
val Int.`1inch`: Money get() = Money.of(this, Currency.of("1INCH"))

class ReadmeExamplesTest : FunSpec({
    test("the BigDecimal comparison: an amount with no currency adds regardless") {
        val priceEur = "19.99".toBigDecimal()
        val shippingUsd = "4.95".toBigDecimal()
        (priceEur + shippingUsd) shouldBe "24.94".toBigDecimal()
    }

    test("the BigDecimal comparison: money in two currencies does not add") {
        val price = 19.99.eur
        val shipping = 4.95.usd
        shouldThrow<MismatchedCurrencyException> { price + shipping }
    }

    test("the BigDecimal comparison: division loses a cent where allocate does not") {
        val total = "24.94".toBigDecimal()
        val third = total.divide(3.toBigDecimal(), 2, RoundingMode.HALF_UP)
        third shouldBe "8.31".toBigDecimal()
        (third * 3.toBigDecimal()) shouldBe "24.93".toBigDecimal() // not 24.94 — a cent is gone

        24.94.eur.allocate(3).map { it.toString() } shouldBe
            listOf("8.32 EUR", "8.31 EUR", "8.31 EUR")
        24.94.eur.allocate(3).reduce(Money::plus) shouldBe 24.94.eur
    }

    test("rounding follows the money's scale") {
        (123.eur * 3).amount.scale() shouldBe 2
        (123.eur(10) * 3).amount.scale() shouldBe 10
    }

    test("quick start") {
        val price = 19.99.eur
        val shipping = 4.95.eur
        (price + shipping).toString() shouldBe "24.94 EUR"
        ((price + shipping) * 3).toString() shouldBe "74.82 EUR"
        (price + shipping).allocate(3).map { it.toString() } shouldBe
            listOf("8.32 EUR", "8.31 EUR", "8.31 EUR")
        listOf(price, shipping).sum().toString() shouldBe "24.94 EUR"
        price.toString() shouldBe "19.99 EUR"
    }

    test("scale table") {
        123.eur.toString() shouldBe "123.00 EUR"
        123.1.eur.toString() shouldBe "123.10 EUR"
        123.123.eur.toString() shouldBe "123.123 EUR"
        123.eur(4).toString() shouldBe "123.0000 EUR"
        "123.1200".eur.toString() shouldBe "123.1200 EUR"
        123.12345.eur(2).toString() shouldBe "123.12 EUR"
    }

    test("equality consequences") {
        (10.eur(2) == 10.eur(4)) shouldBe false
        10.eur(2).hasSameValueAs(10.eur(4)) shouldBe true
        10.eur(2).compareTo(10.eur(4)) shouldBe 0
        setOf(10.eur(2), 10.eur(4)).size shouldBe 2
        listOf(10.eur(2)).contains(10.eur(4)) shouldBe false
        listOf(10.eur(2), 10.eur(4)).distinct().size shouldBe 2
        mapOf(10.eur(2) to "a")[10.eur(4)] shouldBe null
        (10.eur(2) >= 10.eur(4)) shouldBe true
        listOf(10.eur, 5.eur, 7.eur).sorted() shouldBe listOf(5.eur, 7.eur, 10.eur)
    }

    test("rounding and payable") {
        (12.34.eur * 3).toString() shouldBe "37.02 EUR"
        (10.eur / 3).toString() shouldBe "3.33 EUR"
        (10.eur / 4.eur) shouldBe "2.5".toBigDecimal()
        "0.033".money(Currency.of("EUR")).withCurrencyScale().toString() shouldBe "0.03 EUR"
        "0.033".money(Currency.of("EUR")).withCurrencyScale(RoundingMode.UP).toString() shouldBe "0.04 EUR"
        123.eur.withScale(4).toString() shouldBe "123.0000 EUR"
    }

    test("allocation") {
        10.eur.allocate(3).map { it.toString() } shouldBe listOf("3.34 EUR", "3.33 EUR", "3.33 EUR")
        10.eur.allocate(listOf(1, 1, 3)).map { it.toString() } shouldBe
            listOf("2.00 EUR", "2.00 EUR", "6.00 EUR")
        11.eur(0).allocate(3).map { it.toString() } shouldBe listOf("4 EUR", "4 EUR", "3 EUR")
        62.56.eur.allocate(3, scale = 0).map { it.toString() } shouldBe
            listOf("21.56 EUR", "21 EUR", "20 EUR")
        62.56.eur.allocate(3, scale = 0, bias = RemainderBias.LAST).map { it.toString() } shouldBe
            listOf("20 EUR", "21 EUR", "21.56 EUR")
    }

    test("percentages") {
        19.percent.of(12.34.eur).toString() shouldBe "2.34 EUR"
        250.eur.increasedBy(19.percent).toString() shouldBe "297.50 EUR"
        250.eur.decreasedBy(19.percent).toString() shouldBe "202.50 EUR"
        50.eur.asPercentageOf(250.eur) shouldBe 20.percent
    }

    test("added suffixes") {
        1234.thb.toString() shouldBe "1234.00 THB"
        "1234.56".thb.toString() shouldBe "1234.56 THB"
        123.`try`.toString() shouldBe "123.00 TRY"
        123.`1inch`.toString() shouldBe "123.000000000000000000 1INCH"
    }

    test("empty-safe total") {
        Money.total(Currency.of("EUR"), emptyList()).toString() shouldBe "0.00 EUR"
    }

    test("BigDecimal receiver suffix compiles") {
        BigDecimal.ONE.eur.toString() shouldBe "1.00 EUR"
    }
})
