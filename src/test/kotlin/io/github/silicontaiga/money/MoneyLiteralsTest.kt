package io.github.silicontaiga.money

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.RoundingMode

private val EUR = Currency.of("EUR")

class MoneyLiteralsTest : FunSpec({

    test("money reads an Int in the given currency") {
        123.money(EUR).amount shouldBe "123.00".toBigDecimal()
    }

    test("money reads a Long in the given currency") {
        123L.money(EUR).amount shouldBe "123.00".toBigDecimal()
    }

    test("money reads a Double in the given currency") {
        123.1234.money(EUR).amount shouldBe "123.1234".toBigDecimal()
    }

    test("money reads a String in the given currency") {
        "123.1200".money(EUR).amount shouldBe "123.1200".toBigDecimal()
    }

    test("money reads a BigDecimal in the given currency") {
        "123.123".toBigDecimal().money(EUR).amount shouldBe "123.123".toBigDecimal()
    }

    test("money carries the currency it was given") {
        123.money(EUR).currency shouldBe EUR
    }

    test("a suffix is the currency's code, lowercased") {
        123.eur.currency shouldBe Currency.of("EUR")
        123.usd.currency shouldBe Currency.of("USD")
        123.btc.currency shouldBe Currency.of("BTC")
    }

    test("a suffix takes the currency's scale") {
        123.eur.amount shouldBe "123.00".toBigDecimal()
        123.jpy.amount shouldBe "123".toBigDecimal()
        123.btc.amount shouldBe "123.00000000".toBigDecimal()
    }

    test("a suffix reads every literal receiver") {
        123L.eur.amount shouldBe "123.00".toBigDecimal()
        123.1234.eur.amount shouldBe "123.1234".toBigDecimal()
        "123.1200".eur.amount shouldBe "123.1200".toBigDecimal()
        "123.123".toBigDecimal().eur.amount shouldBe "123.123".toBigDecimal()
    }

    test("a suffix takes a named scale") {
        123.eur(4).amount shouldBe "123.0000".toBigDecimal()
    }

    test("a named scale finer than the value needs rounds it") {
        123.12345.eur(2).amount shouldBe "123.12".toBigDecimal()
    }

    test("a named scale takes an explicit rounding mode") {
        123.125.eur(2, RoundingMode.UP).amount shouldBe "123.13".toBigDecimal()
    }
})
