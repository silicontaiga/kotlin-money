package io.github.silicontaiga.money

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.filter
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import java.math.BigDecimal

class MoneyAllocationTest : FunSpec({

    test("an equal split hands the odd unit to the first part") {
        10.eur.allocate(3) shouldBe listOf(3.34.eur, 3.33.eur, 3.33.eur)
    }

    test("an equal split sums to exactly the original") {
        10.eur.allocate(3).reduce(Money::plus) shouldBe 10.eur
    }

    test("a whole-unit currency splits into whole units") {
        10.eur(0).allocate(3) shouldBe listOf(4.eur(0), 3.eur(0), 3.eur(0))
    }

    test("several leftover units are handed out one at a time") {
        11.eur(0).allocate(3) shouldBe listOf(4.eur(0), 4.eur(0), 3.eur(0))
    }

    test("a named scale the amount cannot be written at still preserves the total") {
        62.56.eur.allocate(3, scale = 0) shouldBe listOf("21.56".money(EUR), 21.eur(0), 20.eur(0))
    }

    test("the part carrying the sub-scale remainder has a finer scale than its siblings") {
        val parts = 62.56.eur.allocate(3, scale = 0)
        parts.map { it.amount.scale() } shouldBe listOf(2, 0, 0)
    }
    test("the bias defaults to the first part") {
        10.eur.allocate(3) shouldBe 10.eur.allocate(3, bias = RemainderBias.FIRST)
    }

    test("a last bias hands the leftovers to the far end") {
        10.eur.allocate(3, bias = RemainderBias.LAST) shouldBe listOf(3.33.eur, 3.33.eur, 3.34.eur)
    }

    test("a last bias moves the sub-scale remainder with it") {
        62.56.eur.allocate(3, scale = 0, bias = RemainderBias.LAST) shouldBe
            listOf(20.eur(0), 21.eur(0), "21.56".money(EUR))
    }
    test("a negative amount mirrors the positive case, so a credit note reverses its invoice") {
        (-10).eur.allocate(3) shouldBe 10.eur.allocate(3).map { -it }
    }

    test("a negative split sums to exactly the original") {
        (-10).eur.allocate(3).reduce(Money::plus) shouldBe (-10).eur
    }

    test("a negative amount mirrors at a named scale, keeping the sub-scale remainder") {
        (-62.56).eur.allocate(3, scale = 0) shouldBe 62.56.eur.allocate(3, scale = 0).map { -it }
    }

    test("a negative amount mirrors under a last bias") {
        (-10).eur.allocate(3, bias = RemainderBias.LAST) shouldBe
            10.eur.allocate(3, bias = RemainderBias.LAST).map { -it }
    }
    test("a scale the amount is representable at leaves every part at that scale") {
        10.eur.allocate(3, scale = 0) shouldBe listOf(4.eur(0), 3.eur(0), 3.eur(0))
    }

    test("ratios divide the amount in proportion") {
        10.eur.allocate(listOf(1, 1, 3)) shouldBe listOf(2.eur, 2.eur, 6.eur)
    }

    test("a ratio split sums to exactly the original") {
        10.eur.allocate(listOf(1, 1, 1)).reduce(Money::plus) shouldBe 10.eur
    }

    test("a zero ratio receives nothing, and is skipped when leftovers are handed out") {
        10.eur.allocate(listOf(0, 1, 1, 1)) shouldBe listOf(0.eur, 3.34.eur, 3.33.eur, 3.33.eur)
    }

    test("a ratio split mirrors for a negative amount") {
        (-10).eur.allocate(listOf(1, 1, 3)) shouldBe 10.eur.allocate(listOf(1, 1, 3)).map { -it }
    }

    test("a negative ratio is rejected") {
        shouldThrow<IllegalArgumentException> { 10.eur.allocate(listOf(1, -1, 3)) }
    }

    test("ratios summing to zero are rejected, there being no proportion to divide by") {
        shouldThrow<IllegalArgumentException> { 10.eur.allocate(listOf(0, 0, 0)) }
    }
    test("two ratios give two parts, the case a vararg spelling would have misread") {
        10.eur.allocate(listOf(1, 1)) shouldBe listOf(5.eur, 5.eur)
    }

    test("fewer than one part is rejected, rather than silently losing the amount") {
        shouldThrow<IllegalArgumentException> { 10.eur.allocate(0) }
        shouldThrow<IllegalArgumentException> { 10.eur.allocate(-1) }
    }

    test("an empty ratio list is rejected") {
        shouldThrow<IllegalArgumentException> { 10.eur.allocate(emptyList()) }
    }

    test("an equal allocation always sums to exactly the original value") {
        checkAll(
            Arb.int(-1_000_000..1_000_000),
            Arb.int(1..12),
            Arb.int(0..4),
        ) { cents, parts, scale ->
            val money = moneyOfCents(cents)
            money.allocate(parts, scale = scale).reduce(Money::plus).hasSameValueAs(money) shouldBe true
        }
    }

    test("a ratio allocation always sums to exactly the original value") {
        checkAll(
            Arb.int(-1_000_000..1_000_000),
            Arb.list(Arb.int(0..10), 1..8).filter { it.sum() > 0 },
        ) { cents, ratios ->
            val money = moneyOfCents(cents)
            money.allocate(ratios).reduce(Money::plus).hasSameValueAs(money) shouldBe true
        }
    }

    test("negating and allocating commute, whatever the bias") {
        checkAll(
            Arb.int(-1_000_000..1_000_000),
            Arb.int(1..10),
            Arb.enum<RemainderBias>(),
        ) { cents, parts, bias ->
            val money = moneyOfCents(cents)
            (-money).allocate(parts, bias = bias) shouldBe money.allocate(parts, bias = bias).map { -it }
        }
    }
})

private val EUR = Currency.of("EUR")

private fun moneyOfCents(cents: Int): Money = Money.of(BigDecimal(cents.toLong()).movePointLeft(2), EUR)
