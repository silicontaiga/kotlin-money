package io.github.silicontaiga.money

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

private val BTC = Currency.of("BTC")

class MoneyToStringTest : FunSpec({

    test("toString is the amount, a space, then the code") {
        123.45.eur.toString() shouldBe "123.45 EUR"
    }

    test("toString prints trailing zeros, since scale is part of the value") {
        123.eur.toString() shouldBe "123.00 EUR"
        123.eur(4).toString() shouldBe "123.0000 EUR"
    }

    test("toString uses the code and never the symbol, because a dollar sign says nothing") {
        1.usd.toString() shouldBe "1.00 USD"
        1.jpy.toString() shouldBe "1 JPY"
    }

    test("toString carries the sign") {
        (-1).eur.toString() shouldBe "-1.00 EUR"
    }

    test("toString never resorts to scientific notation") {
        "0.00000001".money(BTC).toString() shouldBe "0.00000001 BTC"
        1.eth.toString() shouldBe "1.000000000000000000 ETH"
    }
})
