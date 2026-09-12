package io.github.silicontaiga.money

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.MathContext
import java.math.RoundingMode

class MoneyArithmeticTest : FunSpec({

    test("addition is exact") {
        (12.34.eur + 0.66.eur) shouldBe 13.eur
    }

    test("addition takes the larger of the two operand scales") {
        (123.eur(4) + 123.eur(6)).amount.scale() shouldBe 6
    }

    test("addition across currencies throws") {
        shouldThrow<MismatchedCurrencyException> { 1.eur + 1.usd }
    }

    test("subtraction is exact") {
        (13.eur - 0.66.eur) shouldBe 12.34.eur
    }

    test("subtraction takes the larger of the two operand scales") {
        (123.eur(4) - 123.eur(6)).amount.scale() shouldBe 6
    }

    test("subtraction across currencies throws") {
        shouldThrow<MismatchedCurrencyException> { 1.eur - 1.usd }
    }

    test("subtraction yields a negative amount, which is legal") {
        (1.eur - 3.eur) shouldBe (-2).eur
    }

    test("negation flips the sign") {
        -(2.eur) shouldBe (-2).eur
    }

    test("negation keeps the scale") {
        (-(2.eur(4))).amount.scale() shouldBe 4
    }

    test("abs makes a negative amount positive") {
        (-2).eur.abs() shouldBe 2.eur
    }

    test("abs leaves a positive amount alone") {
        2.eur.abs() shouldBe 2.eur
    }

    test("multiplication by an Int keeps the money's scale") {
        (12.34.eur * 3) shouldBe 37.02.eur
    }

    test("multiplication accepts every scalar type") {
        (12.34.eur * 3L) shouldBe 37.02.eur
        (12.34.eur * 3.0) shouldBe 37.02.eur
        (12.34.eur * 3.toBigDecimal()) shouldBe 37.02.eur
    }

    test("multiplication rounds to the money's own scale") {
        (10.eur * 0.3333) shouldBe 3.33.eur
    }

    test("multiplication rounds half to even") {
        (0.05.eur * 0.5) shouldBe 0.02.eur
    }

    test("multiplication rounds to a finer money's scale, not the currency's") {
        (10.eur(4) * 0.33333).amount shouldBe "3.3333".toBigDecimal()
    }

    test("division by a scalar keeps the money's scale") {
        (10.eur / 3) shouldBe 3.33.eur
    }

    test("division by a scalar discards what does not fit, unlike allocate") {
        val third = 10.eur / 3
        (third + third + third) shouldBe 9.99.eur
    }

    test("division accepts every scalar type") {
        (10.eur / 4L) shouldBe 2.50.eur
        (10.eur / 4.0) shouldBe 2.50.eur
        (10.eur / 4.toBigDecimal()) shouldBe 2.50.eur
    }

    test("division rounds half to even") {
        (0.05.eur / 2) shouldBe 0.02.eur
    }

    test("division by zero throws rather than returning a sentinel") {
        shouldThrow<ArithmeticException> { 10.eur / 0 }
    }

    test("dividing money by money gives how many times one fits into the other") {
        (10.eur / 4.eur) shouldBe "2.5".toBigDecimal()
    }

    test("dividing money by money requires the same currency") {
        shouldThrow<MismatchedCurrencyException> { 10.eur / 4.usd }
    }

    test("a non-terminating quotient rounds to 34 digits, half to even") {
        (1.eur / 3.eur) shouldBe "0.3333333333333333333333333333333333".toBigDecimal()
    }

    test("dividing money by money takes an explicit context") {
        1.eur.div(3.eur, MathContext(4, RoundingMode.HALF_EVEN)) shouldBe "0.3333".toBigDecimal()
    }

    test("dividing by a zero amount throws") {
        shouldThrow<ArithmeticException> { 10.eur / 0.eur }
    }
})
