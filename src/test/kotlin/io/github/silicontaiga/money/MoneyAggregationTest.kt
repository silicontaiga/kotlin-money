package io.github.silicontaiga.money

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

private val EUR = Currency.of("EUR")

class MoneyAggregationTest : FunSpec({

    test("sum adds every element") {
        listOf(3.34.eur, 3.33.eur).sum() shouldBe 6.67.eur
    }

    test("sum of a single element is that element") {
        listOf(3.34.eur).sum() shouldBe 3.34.eur
    }

    test("sum takes the largest scale among the elements, consistent with plus") {
        listOf(1.eur(2), 1.eur(6)).sum().amount.scale() shouldBe 6
    }

    test("sum across currencies throws") {
        shouldThrow<MismatchedCurrencyException> { listOf(3.eur, 3.usd).sum() }
    }

    test("summing nothing throws, there being no currency to return a zero in") {
        val thrown = shouldThrow<IllegalArgumentException> { emptyList<Money>().sum() }
        thrown.message shouldContain "total"
    }

    test("total names the currency, so summing nothing is well defined") {
        Money.total(EUR, emptyList()) shouldBe 0.eur
    }

    test("total adds every element") {
        Money.total(EUR, listOf(3.34.eur, 3.33.eur)) shouldBe 6.67.eur
    }

    test("total takes the largest scale among the elements") {
        Money.total(EUR, listOf(1.eur(6))).amount.scale() shouldBe 6
    }

    test("total rejects an element in another currency") {
        shouldThrow<MismatchedCurrencyException> { Money.total(EUR, listOf(3.usd)) }
    }
})
