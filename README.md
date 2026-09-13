# kotlin-money

[![Maven Central](https://img.shields.io/maven-central/v/io.github.silicontaiga/kotlin-money?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.silicontaiga/kotlin-money)
[![CI](https://github.com/silicontaiga/kotlin-money/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/silicontaiga/kotlin-money/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/silicontaiga/kotlin-money/branch/main/graph/badge.svg)](https://codecov.io/gh/silicontaiga/kotlin-money)
[![docs](https://img.shields.io/website?url=https%3A%2F%2Fsilicontaiga.github.io%2Fkotlin-money%2Flatest%2F&label=docs)](https://silicontaiga.github.io/kotlin-money/latest/)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)
[![Kotlin](https://img.shields.io/badge/dynamic/toml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fsilicontaiga%2Fkotlin-money%2Fmain%2Fgradle%2Flibs.versions.toml&query=%24.versions.kotlin&label=Kotlin&logo=kotlin&color=7F52FF)](gradle/libs.versions.toml)

Exact, explicit money arithmetic for Kotlin/JVM.

## Why not just `BigDecimal`

Because an amount is not money. Reaching for a general-purpose number here is **primitive
obsession**: a `BigDecimal` can hold `19.99`, but not *19.99 of what*. The currency has to live
somewhere else — an adjacent field, a database column, a variable name, a comment. None of those
is carried by the amount, so nothing can check it:

```kotlin
val priceEur = "19.99".toBigDecimal()
val shippingUsd = "4.95".toBigDecimal()

priceEur + shippingUsd                  // 24.94 — denominated in nothing, and nothing objected
```

`Money` is a **value object**: the amount and its currency are one inseparable value. The currency
travels with the figure, which is what makes a check possible at all:

```kotlin
val price = 19.99.eur
val shipping = 4.95.usd

price + shipping                        // throws MismatchedCurrencyException
```

Both spellings compile. Currency is a runtime value rather than a type parameter — deliberately,
because the set is open and a code read from a database column has to be able to become a
`Currency` at all — so this is a thrown exception and not a compile error. The difference is not
when the mistake is caught but whether anything is in a position to catch it.

Two more things follow from amount and currency being one value.

**Splitting preserves the total.** Dividing a `BigDecimal` rounds each share on its own, so the
shares stop adding up. Allocation is a money operation, and it is exact:

```kotlin
val total = "24.94".toBigDecimal()
total.divide(3.toBigDecimal(), 2, RoundingMode.HALF_UP)   // 8.31 — and 8.31 × 3 is 24.93
24.94.eur.allocate(3)                                     // 8.32, 8.31, 8.31 — exactly 24.94
```

**Rounding follows the money's scale.** `123.eur` rounds to two places, `123.eur(10)` to ten. Each
operation that rounds has an overload taking an explicit scale and `RoundingMode`.

Values are immutable and thread-safe. Negative amounts are legal — ledgers need credits as well as
debits.

While the version stays below `1.0.0` the API may still change — see
[versioning](CONTRIBUTING.md#releases).

## Install

```kotlin
dependencies {
    implementation("io.github.silicontaiga:kotlin-money:0.1.0")
}
```

The Maven Central badge above shows the current release.

Everything lives in one package, and percentages come from the sibling library
[`kotlin-percentage`](https://github.com/silicontaiga/kotlin-percentage), which arrives
transitively — **you do not add it yourself**:

```kotlin
import io.github.silicontaiga.money.*
import io.github.silicontaiga.percentage.*   // only if you apply percentages
```

Java 11 bytecode, so it runs on any JVM from 11 up, and on Android with desugaring. The jar declares
`Automatic-Module-Name: io.github.silicontaiga.money`.

### Snapshots

Every push to `main` publishes a `-SNAPSHOT`, so a fix can be tried before it is released.

Snapshots are **not** on Maven Central proper: they are not indexed by its search and are not
mirrored to `repo1.maven.org`, so looking there will not find them. They live in a separate
repository, which has to be declared:

```kotlin
repositories {
    mavenCentral()
    maven("https://central.sonatype.com/repository/maven-snapshots/")
}

dependencies {
    implementation("io.github.silicontaiga:kotlin-money:0.1.1-SNAPSHOT")
}
```

The snapshot line is always `VERSION_NAME` in [`gradle.properties`](gradle.properties). Gradle caches
snapshots for 24 hours — pass `--refresh-dependencies` to force a re-resolve sooner.

## Quick start

```kotlin
import io.github.silicontaiga.money.*

val price = 19.99.eur
val shipping = 4.95.eur

price + shipping                       // 24.94 EUR
(price + shipping) * 3                 // 74.82 EUR
(price + shipping).allocate(3)         // 8.32, 8.31, 8.31 — sums to exactly 24.94

listOf(price, shipping).sum()          // 24.94 EUR
price.toString()                       // "19.99 EUR"
```

A `Money` is an amount denominated in exactly one currency. Five literal receivers construct one:

```kotlin
123.eur                                // 123.00 EUR — Int
123L.eur                               // 123.00 EUR — Long
123.1234.eur                           // 123.1234 EUR — Double
"123.1200".eur                         // 123.1200 EUR — String, the exact-entry door
"123.12".toBigDecimal().eur            // 123.12 EUR — BigDecimal

Money.of(123, Currency.of("EUR"))      // the general factory, for any currency
123.money(Currency.of("THB"))          // its literal spelling
```

`String` is the only literal that can carry trailing zeros or express an 18-decimal token amount
exactly; a `Double` has lost both before any library code runs.

## Scale is part of the value

Every currency has a scale — 2 for EUR, 0 for JPY, 3 for KWD, 8 for BTC, 18 for ETH — and a Money
**stores the scale it was given**. Trailing zeros are never stripped, because the precision a figure
is quoted at is part of what it says. The currency's scale is a floor, never a cap:

| written | scale | held as |
|---|---|---|
| `123.eur` | 2 — the currency's | `123.00` |
| `123.1.eur` | 2 — the currency's is a floor | `123.10` |
| `123.123.eur` | 3 — the value needs it | `123.123` |
| `123.eur(4)` | 4 — named explicitly | `123.0000` |
| `"123.1200".eur` | 4 — `String` carries trailing zeros | `123.1200` |
| `123.12345.eur(2)` | 2 — named, so the value rounds | `123.12` |

Construction rounds only when an explicit scale demands it.

## Equality: read this one

**Two Money values are `==` only when amount, scale *and* currency match.** This is the rule most
likely to surprise you, so it is worth knowing before you store money in a collection:

```kotlin
10.eur(2) == 10.eur(4)                  // false — scale differs
10.eur(2).hasSameValueAs(10.eur(4))     // true  — amount and currency only
10.eur(2).compareTo(10.eur(4))          // 0     — ordering is by value alone
```

`equals` is therefore inconsistent with `compareTo`, exactly as `BigDecimal`'s is, and the
consequences are real:

```kotlin
setOf(10.eur(2), 10.eur(4)).size            // 2 — a TreeSet would hold 1
listOf(10.eur(2)).contains(10.eur(4))       // false
listOf(10.eur(2), 10.eur(4)).distinct()     // both survive
mapOf(10.eur(2) to "a")[10.eur(4)]          // null
```

**Reach for `hasSameValueAs` whenever scale should not matter**, and normalise with `withScale` or
`withCurrencyScale` before using money as a map key.

Ordering, by contrast, ignores scale — so threshold comparisons behave as you would expect:

```kotlin
10.eur(2) >= 10.eur(4)                  // true
listOf(10.eur, 5.eur, 7.eur).sorted()   // 5.00, 7.00, 10.00
```

## Rounding and payable amounts

Addition and subtraction are exact and take the larger operand scale, so `123.eur(4) + 123.eur(6)`
has scale 6. Multiplication, division and percentage application round to the money's own scale,
`HALF_EVEN` by default, and each offers overloads taking an explicit scale and `RoundingMode`:

```kotlin
12.34.eur * 3                           // 37.02 EUR
10.eur / 3                              // 3.33 EUR — rounds; the remaining cent is LOST
10.eur / 4.eur                          // 2.5 — a plain number, not a Money
```

A Money is not necessarily *payable* — `0.033 EUR` is a legal value. Ask for a settleable figure by
name:

```kotlin
import java.math.RoundingMode

"0.033".money(Currency.of("EUR")).withCurrencyScale()        // 0.03 EUR
"0.033".money(Currency.of("EUR")).withCurrencyScale(RoundingMode.UP)   // 0.04 EUR
123.eur.withScale(4)                                          // 123.0000 EUR
```

## Allocation preserves the total

Splitting money between parties is a different operation from dividing it: **`allocate` loses
nothing, `/` rounds.** Anyone dividing money between people wants `allocate`.

```kotlin
10.eur.allocate(3)                 // 3.34, 3.33, 3.33  — sums to exactly 10.00
10.eur.allocate(listOf(1, 1, 3))   // 2.00, 2.00, 6.00  — by ratio
11.eur(0).allocate(3)              // 4, 4, 3           — two whole units over
```

The invariant holds even when the requested scale cannot express the amount. Parts are computed at
that scale, leftover whole units are handed out one at a time by the bias, and whatever remains
below the scale goes to the biased part:

```kotlin
62.56.eur.allocate(3, scale = 0)                              // 21.56, 21, 20
62.56.eur.allocate(3, scale = 0, bias = RemainderBias.LAST)   // 20, 21, 21.56
```

**The returned parts are therefore not uniform in scale** — the first above has scale 2 and the
others scale 0 — and because scale is part of equality, that is visible to callers. If you need
uniform parts, choose a scale the amount is representable at, or round the money first.

Negative amounts mirror the positive case, so `(-m).allocate(n)` equals `m.allocate(n).map { -it }`
and a credit note still matches the invoice it reverses. Ratios may be zero — a party entitled to
none of this particular split — but not negative, and a ratio list summing to zero throws.

## Percentages

```kotlin
import io.github.silicontaiga.money.*        // of/increasedBy/decreasedBy for Money live here
import io.github.silicontaiga.percentage.*   // .percent lives here

19.percent.of(12.34.eur)           // 2.34 EUR
250.eur.increasedBy(19.percent)    // 297.50 EUR
250.eur.decreasedBy(19.percent)    // 202.50 EUR
50.eur.asPercentageOf(250.eur)     // 20% — same currency required
```

Note that `Percentage.of(Money)` **rounds** to the money's scale, while `Percentage.of(BigDecimal)`
is exact and takes no rounding parameter — 19% of `12.34 EUR` is `2.3446`, and a Money lands on its
scale. Same name, different contract.

## Currencies

A `Currency` carries a **code**, a **scale** and a **symbol**, and nothing else.

```kotlin
Currency.of("EUR")        // throws on an unknown code
Currency.ofOrNull("XYZ")  // null — for boundary code parsing untrusted input

Currencies.iso            // every ISO 4217 currency, withdrawn ones included
Currencies.crypto         // the shipped cryptocurrencies
```

A code identifies exactly one currency, and currency equality is by code alone. Codes are compared
exactly, so `eur` and `EUR` are different codes. The set is deliberately open — circulating fiat,
withdrawn historical currencies, cryptocurrencies and private currencies such as play money all
qualify — and a code need only be non-blank and unique.

The symbol is a **label, not a formatting facility**. Symbols are not unique — `$` belongs to USD,
CAD, AUD, MXN, SGD and HKD alike — so a symbol never identifies a currency and is never parsed back
into one. `Money.toString()` uses the code for the same reason: `$1.00` does not say which dollar.

### Currency suffixes

A suffix is the currency's code, lowercased. Suffixes ship for 28 major currencies, on all five
receivers:

```
usd  eur  gbp  jpy  chf  cad  aud  nzd  cny  hkd  sgd  sek  nok  dkk
pln  czk  huf  inr  brl  mxn  zar  krw  rub  ils  btc  eth  usdt  usdc
```

Every other currency is fully supported through `Money.of` and `123.money(currency)`.

#### Adding a suffix of your own

The shipped set is deliberately small — five receivers across all ~180 ISO codes would be roughly
900 public declarations and would flood completion on `123.`. Adding one follows the same rule,
the code lowercased, and costs a line per receiver you want:

```kotlin
import io.github.silicontaiga.money.Currency
import io.github.silicontaiga.money.Money
import java.math.BigDecimal

val Int.thb: Money get() = Money.of(this, Currency.of("THB"))
val Long.thb: Money get() = Money.of(this, Currency.of("THB"))
val Double.thb: Money get() = Money.of(this, Currency.of("THB"))
val String.thb: Money get() = Money.of(this, Currency.of("THB"))
val BigDecimal.thb: Money get() = Money.of(this, Currency.of("THB"))

1234.thb            // 1234.00 THB
"1234.56".thb       // 1234.56 THB
```

**Resolve the currency inside the getter, as above, rather than caching it in a top-level `val`.**
A top-level property initialises when its file is first touched, which may be before
[`Money.configure`](#registering-your-own-currencies) runs. If configuration then replaces that
currency, the cached object is the old definition — and since currency equality is by identity, money
built from it will not combine with money built from the new one.

Some codes do not lowercase into usable identifiers, and the rule then takes backticks rather than
an alias:

```kotlin
val Int.`try`: Money get() = Money.of(this, Currency.of("TRY"))      // a Kotlin hard keyword
val Int.`1inch`: Money get() = Money.of(this, Currency.of("1INCH"))  // starts with a digit

123.`try`           // 123.00 TRY
123.`1inch`         // 123.000000000000000000 1INCH
```

### Registering your own currencies

The shipped set can be extended or replaced **once, at startup, before any Money exists**:

```kotlin
Money.configure {
    replaceCurrency(code = "EUR", scale = 4, symbol = "€")
    addCurrency(code = "MONOPOLY", scale = 0, symbol = "M")
}

val Monopoly = Currency.of("MONOPOLY")
val Int.monopoly: Money get() = Money.of(this, Monopoly)
```

`addCurrency` throws if the code already exists; `replaceCurrency` throws if it does not. A second
`configure` call, or any call after a Money has been constructed, throws.

**This is an application-level facility, and a library must never call it** — reconfiguring
currencies changes the meaning of its consumer's money. Tests that use it affect the whole JVM and
need isolating.

## Currency mismatch

Combining two Money values in different currencies is always an error:

```kotlin
3.eur + 3.usd                                    // throws MismatchedCurrencyException
3.eur < 3.usd                                    // throws — there is no order between them
listOf(3.eur, 3.usd).sum()                       // throws
emptyList<Money>().sum()                         // throws — no currency to return a zero in
Money.total(Currency.of("EUR"), emptyList())     // 0.00 EUR — the empty-safe form
```

There is no failure type to unwrap: mixing currencies is a programming error, not a runtime
condition every correct caller should have to handle. Comparison throws for the same reason addition
does, which means a mixed-currency list cannot be sorted.

`MismatchedCurrencyException` is the only exception this library defines. Everything else reuses the
JDK: `ArithmeticException` for division by zero, `IllegalArgumentException` for a negative ratio or
an all-zero ratio list.

## API

| | |
|---|---|
| Construct | `123.eur`, `123.eur(4)`, `"123.12".eur`, `Money.of(123, currency)`, `123.money(currency)` — each from `Int`, `Long`, `Double`, `String` or `BigDecimal` |
| Read | `amount`, `currency` |
| Combine | `a + b`, `a - b`, `-m`, `m * 2`, `m / 2`, `m / other` (→ a plain number) |
| Allocate | `m.allocate(n)`, `m.allocate(ratios: List<Int>)`, each with optional `scale` and `bias` |
| Compare | `Comparable<Money>`; `a == b` (scale-sensitive), `a.hasSameValueAs(b)`, `compareTo` by value |
| Helpers | `abs()`, `unaryMinus()`, `isZero`, `isPositive`, `isNegative`, `withScale(scale, mode)`, `withCurrencyScale(mode)` |
| Aggregate | `sum()` over any `Iterable<Money>`, `Money.total(currency, iterable)` |
| Apply | `p.of(m)`, `m.increasedBy(p)`, `m.decreasedBy(p)`, `m.asPercentageOf(other)` |
| Currencies | `Currency.of(code)`, `Currency.ofOrNull(code)`, `Currencies.iso`, `Currencies.crypto`, `Money.configure { … }` |

The table is a summary; the generated API reference documents every overload, its rounding and what
it throws. It is published per release, so a version can be read at the version you actually depend
on:

- [Latest release](https://silicontaiga.github.io/kotlin-money/latest/)
- A specific one at `https://silicontaiga.github.io/kotlin-money/<version>/` — for example
  [0.1.0](https://silicontaiga.github.io/kotlin-money/0.1.0/)

## Out of scope

- **Formatting and parsing.** `toString()` is debug output: `"123.45 EUR"`, locale-independent,
  never abbreviated, never in scientific notation. That is locale work, not money work — use
  `java.text` for anything a person reads.
- **Currency conversion and exchange rates.** A domain of its own, with rate sources, freshness and
  bid/ask spreads to model.
- **Serialization.** Left to the caller's own framework.
- **Multiplatform.** JVM only, because the exactness comes from `java.math`.

## Building

```bash
./gradlew check   # tests, 100% coverage gate, API dump, ktlint, detekt — the same as CI
```

The build pins itself to JDK 21 (`gradle/gradle-daemon-jvm.properties`) and downloads it if you do
not have it; the artifact targets Java 11.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md). Development is test-driven: every behaviour arrives as a
failing test first.

## License

[Apache-2.0](LICENSE)
