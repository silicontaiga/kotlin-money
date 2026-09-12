# kotlin-money

[![Maven Central](https://img.shields.io/maven-central/v/io.github.silicontaiga/kotlin-money?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.silicontaiga/kotlin-money)
[![CI](https://github.com/silicontaiga/kotlin-money/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/silicontaiga/kotlin-money/actions/workflows/ci.yml)
[![codecov](https://codecov.io/gh/silicontaiga/kotlin-money/branch/main/graph/badge.svg)](https://codecov.io/gh/silicontaiga/kotlin-money)
[![docs](https://img.shields.io/website?url=https%3A%2F%2Fsilicontaiga.github.io%2Fkotlin-money%2Flatest%2F&label=docs)](https://silicontaiga.github.io/kotlin-money/latest/)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)
[![Kotlin](https://img.shields.io/badge/dynamic/toml?url=https%3A%2F%2Fraw.githubusercontent.com%2Fsilicontaiga%2Fkotlin-money%2Fmain%2Fgradle%2Flibs.versions.toml&query=%24.versions.kotlin&label=Kotlin&logo=kotlin&color=7F52FF)](gradle/libs.versions.toml)

Exact, explicit money arithmetic for Kotlin/JVM.

While the version stays below `1.0.0` the API may still change — see
[versioning](CONTRIBUTING.md#releases).

## Usage

A `Money` is an amount denominated in exactly one currency:

```kotlin
123.eur                  // 123.00 EUR — Int
123L.eur                 // 123.00 EUR — Long
123.1234.eur             // 123.1234 EUR — Double
"123.1200".eur           // 123.1200 EUR — String, the exact-entry door
someBigDecimal.eur       // BigDecimal

Money.of(123, Monopoly)  // the general factory
123.money(Monopoly)      // its literal spelling, for any currency
```

Arithmetic is exact where it can be and rounds where it must:

```kotlin
12.34.eur + 0.66.eur     // 13.00 EUR — exact
12.34.eur * 3            // 37.02 EUR — rounds to the money's scale
10.eur / 3               // 3.33 EUR  — rounds; the remaining cent is LOST
10.eur / 4.eur           // 2.5       — a plain number

listOf(3.34.eur, 3.33.eur).sum()   // 6.67 EUR
```

Splitting money between parties is a different operation from dividing it, and the difference
matters: `allocate` loses nothing, `/` rounds.

```kotlin
10.eur.allocate(3)                 // 3.34, 3.33, 3.33  — sums to exactly 10.00
10.eur.allocate(listOf(1, 1, 3))   // 2.00, 2.00, 6.00  — by ratio
11.eur(0).allocate(3)              // 4, 4, 3           — two whole units over
62.56.eur.allocate(3, scale = 0)   // 21.56, 21, 20     — see below
```

Percentages come from the sibling library
[`kotlin-percentage`](https://github.com/silicontaiga/kotlin-percentage):

```kotlin
19.percent.of(12.34.eur)           // 2.34 EUR
250.eur.increasedBy(19.percent)    // 297.50 EUR
250.eur.decreasedBy(19.percent)    // 202.50 EUR
50.eur.asPercentageOf(250.eur)     // 20% — same currency required
```

Note that `Percentage.of(Money)` **rounds** to the money's scale, while `Percentage.of(BigDecimal)`
is exact and takes no rounding parameter — 19% of `12.34 EUR` is `2.3446`, and a Money lands on its
scale. Same name, different contract.

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

### Two equalities

```kotlin
10.eur(2) == 10.eur(4)                  // false — scale differs
10.eur(2).hasSameValueAs(10.eur(4))     // true  — amount and currency only
10.eur(2).compareTo(10.eur(4))          // 0     — ordering is by value alone
```

**`equals` is therefore inconsistent with `compareTo`, exactly as `BigDecimal`'s is.** This is a
deliberate choice, and its consequences are real: a `HashSet` holding `10.eur(2)` and `10.eur(4)` has
two entries where a `TreeSet` has one, `contains` and `distinct()` behave accordingly, and a map
keyed on one will not find the other. Reach for `hasSameValueAs` when scale should not matter.

The alternative — a `compareTo` that breaks ties on scale — was rejected because it would make
`10.eur(2) >= 10.eur(4)` evaluate to `false` for two equal amounts, silently breaking every threshold
comparison in a caller's business logic. Awkward collections are a smaller price than wrong answers
about money.

### Rounding

Addition and subtraction are exact and take the larger operand scale, so `123.eur(4) + 123.eur(6)`
has scale 6. Multiplication, division and percentage application round to the money's own scale,
`HALF_EVEN` by default, and each offers overloads taking an explicit scale and `RoundingMode`.

A Money is not necessarily *payable* — `0.033 EUR` is a legal value. Ask for a settleable figure by
name, with `withScale(scale, mode)` or `withCurrencyScale(mode)`.

## Allocation preserves the total

`allocate` splits one Money into parts that sum to **exactly** the original. That invariant holds
even when the requested scale cannot express the amount:

```kotlin
62.56.eur.allocate(3, scale = 0)   // 21.56, 21, 20 — sums to 62.56
```

Parts are computed at the requested scale, leftover whole units are handed out one at a time by the
bias, and whatever remains below that scale goes to the biased part. **The returned parts are
therefore not uniform in scale**, and because scale is part of equality, that is visible to callers.
A caller who needs uniform parts should choose a scale the amount is representable at, or round the
money first.

```kotlin
62.56.eur.allocate(3, scale = 0, bias = RemainderBias.LAST)   // 20, 21, 21.56
```

Ratios arrive as a list rather than as loose arguments. A `vararg Int` would collide with the
equal-split overload at two positional arguments — Kotlin resolves `allocate(1, 1)` to
`parts = 1, scale = 1`, so a caller asking for a 50/50 split would silently receive one undivided
part. Differing by parameter type instead puts resolution beyond doubt, and `allocate(1, 1, 3)` is
now a compile error rather than a wrong answer.

Negative amounts mirror the positive case, so `(-m).allocate(n)` equals `m.allocate(n).map { -it }`
and a credit note still matches the invoice it reverses. Ratios may be zero — a party entitled to
none of this particular split — but not negative, and a ratio list summing to zero throws.

## Currencies

A `Currency` carries a **code**, a **scale** and a **symbol**, and nothing else.

```kotlin
Currency.of("EUR")        // throws on an unknown code
Currency.ofOrNull("XYZ")  // null — for boundary code parsing untrusted input
```

A code identifies exactly one currency, and currency equality is by code alone. The library ships
definitions for every ISO 4217 currency plus a set of cryptocurrencies, exposed as `Currencies.iso`
and `Currencies.crypto`.

The set is deliberately open — circulating fiat, withdrawn historical currencies, cryptocurrencies
and private currencies such as play money all qualify. A code has **no required shape**: it must be
non-blank and unique, and that is the whole contract, because three uppercase letters would reject
`MONOPOLY`. Codes are compared exactly, so `eur` and `EUR` are different codes.

The symbol is a **label, not a formatting facility**. Symbols are not unique — `$` belongs to USD,
CAD, AUD, MXN, SGD and HKD alike — so a symbol never identifies a currency and is never parsed back
into one. `Money.toString()` uses the code for the same reason: `$1.00` does not say which dollar.

### Registering your own

The shipped set can be extended or replaced **once, at startup**, before any Money exists:

```kotlin
Money.configure {
    replaceCurrency(code = "EUR", scale = 4, symbol = "€")
    addCurrency(code = "MONOPOLY", scale = 0, symbol = "M")
}

val Monopoly = Currency.of("MONOPOLY")
val Int.monopoly get() = Money.of(this, Monopoly)
```

`addCurrency` throws if the code already exists and `replaceCurrency` throws if it does not: one
combined call would let a typo — `replaceCurrency("EURO", …)` — silently invent a currency rather
than fail. A second `configure` call, or any call after a Money has been constructed, throws.

**This is an application-level facility, and a library must never call it** — reconfiguring
currencies changes the meaning of its consumer's money. Tests that use it affect the whole JVM and
need isolating.

### Currency suffixes

A suffix is the currency's code, lowercased: `EUR` gives `eur`, `BTC` gives `btc`. English names are
rejected, because `dollar` is ambiguous across USD, CAD, AUD, NZD, SGD and HKD.

Suffixes ship for a couple of dozen major currencies rather than all ~180 ISO codes: five receivers
across every code would be roughly 900 public declarations, each a binary-compatibility commitment,
and would flood completion on `123.`. Every other currency is fully supported through `Money.of` and
`123.money(currency)`, and a caller adds their own in one line:

```kotlin
val Int.monopoly get() = Money.of(this, Monopoly)
```

Because codes are unconstrained, a lowercased code is not always a usable identifier, and the rule
then needs backticks rather than an exception — `` 123.`try` `` for the Turkish lira, whose code
lowercases to a Kotlin hard keyword, and `` 123.`1inch` `` for a code beginning with a digit.

## Currency mismatch

Combining two Money values in different currencies is always an error:

```kotlin
3.eur + 3.usd                  // throws MismatchedCurrencyException
3.eur < 3.usd                  // throws — there is no order between dollars and euros
listOf(3.eur, 3.usd).sum()     // throws
emptyList<Money>().sum()       // throws — no currency to return a zero in
Money.total(EUR, emptyList())  // 0.00 EUR — the empty-safe form
```

There is no failure type to unwrap: mixing currencies is a programming error, not a runtime
condition every correct caller should have to handle. Comparison throws for the same reason addition
does, which means a mixed-currency list cannot be sorted — the right outcome. Ordering by currency
code and then by amount would give a total order and let any list sort, at the price of
`10.eur < 5.usd` evaluating to `true`.

`MismatchedCurrencyException` is the only exception this library defines. Everything else reuses the
JDK: `ArithmeticException` for division by zero, `IllegalArgumentException` for a negative ratio or
an all-zero ratio list.

## API

| | |
|---|---|
| Construct | `123.eur`, `123.eur(4)`, `"123.12".eur`, `Money.of(123, EUR)`, `123.money(EUR)` — each from `Int`, `Long`, `Double`, `String` or `BigDecimal` |
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

## Install

```kotlin
dependencies {
    implementation("io.github.silicontaiga:kotlin-money:<version>")
}
```

The Maven Central badge above shows the current release.

One runtime dependency, `io.github.silicontaiga:kotlin-percentage`, exported on the API classpath —
applying a percentage to money is close to the point of the library, so it lives in core rather than
behind a second dependency and an extra import.

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

The snapshot line is always `VERSION_NAME` in [`gradle.properties`](gradle.properties): the release
workflow overrides that value only for a tagged release, so `main` always carries the next
`-SNAPSHOT`. Gradle caches snapshots for 24 hours — pass `--refresh-dependencies` to force a
re-resolve sooner.

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
