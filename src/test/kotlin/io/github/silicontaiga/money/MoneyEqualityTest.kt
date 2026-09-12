package io.github.silicontaiga.money

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain

class MoneyEqualityTest : FunSpec({

    test("two moneys are equal when amount, scale and currency all match") {
        10.eur shouldBe 10.eur
    }

    test("moneys differing only in scale are not equal") {
        10.eur(2) shouldNotBe 10.eur(4)
    }

    test("moneys differing only in currency are not equal") {
        10.eur shouldNotBe 10.usd
    }

    test("equal moneys share a hash code") {
        10.eur.hashCode() shouldBe 10.eur.hashCode()
    }

    test("moneys differing in scale hash apart, so a HashSet holds both") {
        setOf(10.eur(2), 10.eur(4)).size shouldBe 2
    }

    test("hasSameValueAs ignores scale") {
        10.eur(2).hasSameValueAs(10.eur(4)) shouldBe true
    }

    test("hasSameValueAs is false for a different amount") {
        10.eur.hasSameValueAs(11.eur) shouldBe false
    }

    test("hasSameValueAs is false across currencies rather than throwing") {
        10.eur.hasSameValueAs(10.usd) shouldBe false
    }

    test("compareTo orders by value alone, ignoring scale") {
        10.eur(2).compareTo(10.eur(4)) shouldBe 0
    }

    test("compareTo orders by amount") {
        (5.eur < 10.eur) shouldBe true
        (10.eur > 5.eur) shouldBe true
    }

    test("a threshold comparison holds across scales, which a scale tiebreak would have broken") {
        (10.eur(2) >= 10.eur(4)) shouldBe true
    }

    test("moneys in one currency sort by amount") {
        listOf(10.eur, 5.eur, 7.eur).sorted() shouldBe listOf(5.eur, 7.eur, 10.eur)
    }

    test("comparing across currencies throws, so a mixed list cannot be sorted") {
        shouldThrow<MismatchedCurrencyException> { 10.eur < 5.usd }
    }

    test("the mismatch names both currencies by code") {
        val thrown = shouldThrow<MismatchedCurrencyException> { 10.eur < 5.usd }
        thrown.message shouldContain "EUR"
        thrown.message shouldContain "USD"
    }

    test("a money equals itself") {
        val money = 10.eur
        (money == money) shouldBe true
    }

    test("a money does not equal a value that is not money") {
        10.eur shouldNotBe "10.00 EUR"
    }
})
