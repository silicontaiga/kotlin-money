package io.github.silicontaiga.money

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.math.RoundingMode

private val EUR = Currency.of("EUR")

class MoneyConstructionTest : FunSpec({

    test("of takes the currency's scale when none is named") {
        Money.of(123, EUR).amount shouldBe "123.00".toBigDecimal()
    }

    test("of carries the currency it was given") {
        Money.of(123, EUR).currency shouldBe EUR
    }

    test("of keeps the places a value needs beyond the currency's scale") {
        Money.of("123.123".toBigDecimal(), EUR).amount shouldBe "123.123".toBigDecimal()
    }

    test("of raises a value that is quoted more coarsely than its currency") {
        Money.of("123.1".toBigDecimal(), EUR).amount shouldBe "123.10".toBigDecimal()
    }

    test("of takes a named scale finer than the currency's") {
        Money.of("123".toBigDecimal(), EUR, scale = 4).amount shouldBe "123.0000".toBigDecimal()
    }

    test("of rounds to a named scale the value exceeds") {
        Money.of("123.12345".toBigDecimal(), EUR, scale = 2).amount shouldBe "123.12".toBigDecimal()
    }

    test("of rounds half to even at a tie") {
        Money.of("0.125".toBigDecimal(), EUR, scale = 2).amount shouldBe "0.12".toBigDecimal()
        Money.of("0.135".toBigDecimal(), EUR, scale = 2).amount shouldBe "0.14".toBigDecimal()
    }

    test("of takes an explicit rounding mode") {
        Money
            .of("0.125".toBigDecimal(), EUR, scale = 2, roundingMode = RoundingMode.UP)
            .amount shouldBe "0.13".toBigDecimal()
    }

    test("of accepts a Long") {
        Money.of(123L, EUR).amount shouldBe "123.00".toBigDecimal()
    }

    test("of accepts a Double") {
        Money.of(123.1234, EUR).amount shouldBe "123.1234".toBigDecimal()
    }

    test("of reads a Double as the decimal it was written as, not its binary expansion") {
        Money.of(0.1, EUR).amount shouldBe "0.10".toBigDecimal()
    }

    test("of rejects a Double that is not a finite number") {
        shouldThrow<IllegalArgumentException> { Money.of(Double.NaN, EUR) }
        shouldThrow<IllegalArgumentException> { Money.of(Double.POSITIVE_INFINITY, EUR) }
        shouldThrow<IllegalArgumentException> { Money.of(Double.NEGATIVE_INFINITY, EUR) }
    }

    test("of accepts a String, which is the only literal carrying trailing zeros") {
        Money.of("123.1200", EUR).amount shouldBe "123.1200".toBigDecimal()
    }

    test("of rejects a String that is not a number") {
        shouldThrow<NumberFormatException> { Money.of("twelve", EUR) }
    }

    test("of takes a named scale for an Int amount") {
        Money.of(123, EUR, scale = 4).amount shouldBe "123.0000".toBigDecimal()
    }

    test("of takes a named scale and rounding mode for an Int amount") {
        Money.of(123, EUR, scale = 0, roundingMode = RoundingMode.UP).amount shouldBe "123".toBigDecimal()
    }
})
