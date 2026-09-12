package io.github.silicontaiga.money

import java.math.BigDecimal

// A suffix is the currency's code, lowercased: EUR gives `eur`, BTC gives `btc`. English names
// are rejected, because `dollar` is ambiguous across USD, CAD, AUD, NZD, SGD and HKD.
//
// Suffixes ship for a couple of dozen major currencies rather than all ~180 ISO codes: five
// receivers across every code would be roughly 900 public declarations, each a binary
// compatibility commitment, and would flood completion on `123.`. Every other currency is fully
// supported through `Money.of` and `123.money(currency)`, and a caller adds their own in one line:
//
//     val Int.monopoly get() = Money.of(this, Monopoly)
//
// The currency is resolved on each access rather than cached, so a definition replaced through
// `Money.configure` is what these literals produce.

/**
 * This number as US dollars, so `123.usd` is US dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.usd: Money
    get() = Money.of(this, Currency.of("USD"))

/**
 * This number as US dollars, so `123L.usd` is US dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.usd: Money
    get() = Money.of(this, Currency.of("USD"))

/**
 * This number as US dollars, so `123.45.usd` is US dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.usd: Money
    get() = Money.of(this, Currency.of("USD"))

/**
 * This number as US dollars, so `"123.45".usd` is US dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.usd: Money
    get() = Money.of(this, Currency.of("USD"))

/**
 * This number as US dollars, so `"123.45".toBigDecimal().usd` is US dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.usd: Money
    get() = Money.of(this, Currency.of("USD"))

/**
 * This number as euros, so `123.eur` is euros at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.eur: Money
    get() = Money.of(this, Currency.of("EUR"))

/**
 * This number as euros, so `123L.eur` is euros at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.eur: Money
    get() = Money.of(this, Currency.of("EUR"))

/**
 * This number as euros, so `123.45.eur` is euros at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.eur: Money
    get() = Money.of(this, Currency.of("EUR"))

/**
 * This number as euros, so `"123.45".eur` is euros at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.eur: Money
    get() = Money.of(this, Currency.of("EUR"))

/**
 * This number as euros, so `"123.45".toBigDecimal().eur` is euros at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.eur: Money
    get() = Money.of(this, Currency.of("EUR"))

/**
 * This number as pounds sterling, so `123.gbp` is pounds sterling at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.gbp: Money
    get() = Money.of(this, Currency.of("GBP"))

/**
 * This number as pounds sterling, so `123L.gbp` is pounds sterling at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.gbp: Money
    get() = Money.of(this, Currency.of("GBP"))

/**
 * This number as pounds sterling, so `123.45.gbp` is pounds sterling at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.gbp: Money
    get() = Money.of(this, Currency.of("GBP"))

/**
 * This number as pounds sterling, so `"123.45".gbp` is pounds sterling at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.gbp: Money
    get() = Money.of(this, Currency.of("GBP"))

/**
 * This number as pounds sterling, so `"123.45".toBigDecimal().gbp` is pounds sterling at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.gbp: Money
    get() = Money.of(this, Currency.of("GBP"))

/**
 * This number as yen, so `123.jpy` is yen at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.jpy: Money
    get() = Money.of(this, Currency.of("JPY"))

/**
 * This number as yen, so `123L.jpy` is yen at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.jpy: Money
    get() = Money.of(this, Currency.of("JPY"))

/**
 * This number as yen, so `123.45.jpy` is yen at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.jpy: Money
    get() = Money.of(this, Currency.of("JPY"))

/**
 * This number as yen, so `"123.45".jpy` is yen at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.jpy: Money
    get() = Money.of(this, Currency.of("JPY"))

/**
 * This number as yen, so `"123.45".toBigDecimal().jpy` is yen at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.jpy: Money
    get() = Money.of(this, Currency.of("JPY"))

/**
 * This number as Swiss francs, so `123.chf` is Swiss francs at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.chf: Money
    get() = Money.of(this, Currency.of("CHF"))

/**
 * This number as Swiss francs, so `123L.chf` is Swiss francs at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.chf: Money
    get() = Money.of(this, Currency.of("CHF"))

/**
 * This number as Swiss francs, so `123.45.chf` is Swiss francs at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.chf: Money
    get() = Money.of(this, Currency.of("CHF"))

/**
 * This number as Swiss francs, so `"123.45".chf` is Swiss francs at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.chf: Money
    get() = Money.of(this, Currency.of("CHF"))

/**
 * This number as Swiss francs, so `"123.45".toBigDecimal().chf` is Swiss francs at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.chf: Money
    get() = Money.of(this, Currency.of("CHF"))

/**
 * This number as Canadian dollars, so `123.cad` is Canadian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.cad: Money
    get() = Money.of(this, Currency.of("CAD"))

/**
 * This number as Canadian dollars, so `123L.cad` is Canadian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.cad: Money
    get() = Money.of(this, Currency.of("CAD"))

/**
 * This number as Canadian dollars, so `123.45.cad` is Canadian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.cad: Money
    get() = Money.of(this, Currency.of("CAD"))

/**
 * This number as Canadian dollars, so `"123.45".cad` is Canadian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.cad: Money
    get() = Money.of(this, Currency.of("CAD"))

/**
 * This number as Canadian dollars, so `"123.45".toBigDecimal().cad` is Canadian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.cad: Money
    get() = Money.of(this, Currency.of("CAD"))

/**
 * This number as Australian dollars, so `123.aud` is Australian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.aud: Money
    get() = Money.of(this, Currency.of("AUD"))

/**
 * This number as Australian dollars, so `123L.aud` is Australian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.aud: Money
    get() = Money.of(this, Currency.of("AUD"))

/**
 * This number as Australian dollars, so `123.45.aud` is Australian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.aud: Money
    get() = Money.of(this, Currency.of("AUD"))

/**
 * This number as Australian dollars, so `"123.45".aud` is Australian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.aud: Money
    get() = Money.of(this, Currency.of("AUD"))

/**
 * This number as Australian dollars, so `"123.45".toBigDecimal().aud` is Australian dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.aud: Money
    get() = Money.of(this, Currency.of("AUD"))

/**
 * This number as New Zealand dollars, so `123.nzd` is New Zealand dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.nzd: Money
    get() = Money.of(this, Currency.of("NZD"))

/**
 * This number as New Zealand dollars, so `123L.nzd` is New Zealand dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.nzd: Money
    get() = Money.of(this, Currency.of("NZD"))

/**
 * This number as New Zealand dollars, so `123.45.nzd` is New Zealand dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.nzd: Money
    get() = Money.of(this, Currency.of("NZD"))

/**
 * This number as New Zealand dollars, so `"123.45".nzd` is New Zealand dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.nzd: Money
    get() = Money.of(this, Currency.of("NZD"))

/**
 * This number as New Zealand dollars, so `"123.45".toBigDecimal().nzd` is New Zealand dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.nzd: Money
    get() = Money.of(this, Currency.of("NZD"))

/**
 * This number as renminbi, so `123.cny` is renminbi at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.cny: Money
    get() = Money.of(this, Currency.of("CNY"))

/**
 * This number as renminbi, so `123L.cny` is renminbi at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.cny: Money
    get() = Money.of(this, Currency.of("CNY"))

/**
 * This number as renminbi, so `123.45.cny` is renminbi at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.cny: Money
    get() = Money.of(this, Currency.of("CNY"))

/**
 * This number as renminbi, so `"123.45".cny` is renminbi at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.cny: Money
    get() = Money.of(this, Currency.of("CNY"))

/**
 * This number as renminbi, so `"123.45".toBigDecimal().cny` is renminbi at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.cny: Money
    get() = Money.of(this, Currency.of("CNY"))

/**
 * This number as Hong Kong dollars, so `123.hkd` is Hong Kong dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.hkd: Money
    get() = Money.of(this, Currency.of("HKD"))

/**
 * This number as Hong Kong dollars, so `123L.hkd` is Hong Kong dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.hkd: Money
    get() = Money.of(this, Currency.of("HKD"))

/**
 * This number as Hong Kong dollars, so `123.45.hkd` is Hong Kong dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.hkd: Money
    get() = Money.of(this, Currency.of("HKD"))

/**
 * This number as Hong Kong dollars, so `"123.45".hkd` is Hong Kong dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.hkd: Money
    get() = Money.of(this, Currency.of("HKD"))

/**
 * This number as Hong Kong dollars, so `"123.45".toBigDecimal().hkd` is Hong Kong dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.hkd: Money
    get() = Money.of(this, Currency.of("HKD"))

/**
 * This number as Singapore dollars, so `123.sgd` is Singapore dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.sgd: Money
    get() = Money.of(this, Currency.of("SGD"))

/**
 * This number as Singapore dollars, so `123L.sgd` is Singapore dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.sgd: Money
    get() = Money.of(this, Currency.of("SGD"))

/**
 * This number as Singapore dollars, so `123.45.sgd` is Singapore dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.sgd: Money
    get() = Money.of(this, Currency.of("SGD"))

/**
 * This number as Singapore dollars, so `"123.45".sgd` is Singapore dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.sgd: Money
    get() = Money.of(this, Currency.of("SGD"))

/**
 * This number as Singapore dollars, so `"123.45".toBigDecimal().sgd` is Singapore dollars at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.sgd: Money
    get() = Money.of(this, Currency.of("SGD"))

/**
 * This number as Swedish kronor, so `123.sek` is Swedish kronor at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.sek: Money
    get() = Money.of(this, Currency.of("SEK"))

/**
 * This number as Swedish kronor, so `123L.sek` is Swedish kronor at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.sek: Money
    get() = Money.of(this, Currency.of("SEK"))

/**
 * This number as Swedish kronor, so `123.45.sek` is Swedish kronor at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.sek: Money
    get() = Money.of(this, Currency.of("SEK"))

/**
 * This number as Swedish kronor, so `"123.45".sek` is Swedish kronor at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.sek: Money
    get() = Money.of(this, Currency.of("SEK"))

/**
 * This number as Swedish kronor, so `"123.45".toBigDecimal().sek` is Swedish kronor at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.sek: Money
    get() = Money.of(this, Currency.of("SEK"))

/**
 * This number as Norwegian kroner, so `123.nok` is Norwegian kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.nok: Money
    get() = Money.of(this, Currency.of("NOK"))

/**
 * This number as Norwegian kroner, so `123L.nok` is Norwegian kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.nok: Money
    get() = Money.of(this, Currency.of("NOK"))

/**
 * This number as Norwegian kroner, so `123.45.nok` is Norwegian kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.nok: Money
    get() = Money.of(this, Currency.of("NOK"))

/**
 * This number as Norwegian kroner, so `"123.45".nok` is Norwegian kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.nok: Money
    get() = Money.of(this, Currency.of("NOK"))

/**
 * This number as Norwegian kroner, so `"123.45".toBigDecimal().nok` is Norwegian kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.nok: Money
    get() = Money.of(this, Currency.of("NOK"))

/**
 * This number as Danish kroner, so `123.dkk` is Danish kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.dkk: Money
    get() = Money.of(this, Currency.of("DKK"))

/**
 * This number as Danish kroner, so `123L.dkk` is Danish kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.dkk: Money
    get() = Money.of(this, Currency.of("DKK"))

/**
 * This number as Danish kroner, so `123.45.dkk` is Danish kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.dkk: Money
    get() = Money.of(this, Currency.of("DKK"))

/**
 * This number as Danish kroner, so `"123.45".dkk` is Danish kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.dkk: Money
    get() = Money.of(this, Currency.of("DKK"))

/**
 * This number as Danish kroner, so `"123.45".toBigDecimal().dkk` is Danish kroner at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.dkk: Money
    get() = Money.of(this, Currency.of("DKK"))

/**
 * This number as złoty, so `123.pln` is złoty at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.pln: Money
    get() = Money.of(this, Currency.of("PLN"))

/**
 * This number as złoty, so `123L.pln` is złoty at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.pln: Money
    get() = Money.of(this, Currency.of("PLN"))

/**
 * This number as złoty, so `123.45.pln` is złoty at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.pln: Money
    get() = Money.of(this, Currency.of("PLN"))

/**
 * This number as złoty, so `"123.45".pln` is złoty at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.pln: Money
    get() = Money.of(this, Currency.of("PLN"))

/**
 * This number as złoty, so `"123.45".toBigDecimal().pln` is złoty at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.pln: Money
    get() = Money.of(this, Currency.of("PLN"))

/**
 * This number as Czech koruny, so `123.czk` is Czech koruny at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.czk: Money
    get() = Money.of(this, Currency.of("CZK"))

/**
 * This number as Czech koruny, so `123L.czk` is Czech koruny at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.czk: Money
    get() = Money.of(this, Currency.of("CZK"))

/**
 * This number as Czech koruny, so `123.45.czk` is Czech koruny at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.czk: Money
    get() = Money.of(this, Currency.of("CZK"))

/**
 * This number as Czech koruny, so `"123.45".czk` is Czech koruny at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.czk: Money
    get() = Money.of(this, Currency.of("CZK"))

/**
 * This number as Czech koruny, so `"123.45".toBigDecimal().czk` is Czech koruny at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.czk: Money
    get() = Money.of(this, Currency.of("CZK"))

/**
 * This number as forint, so `123.huf` is forint at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.huf: Money
    get() = Money.of(this, Currency.of("HUF"))

/**
 * This number as forint, so `123L.huf` is forint at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.huf: Money
    get() = Money.of(this, Currency.of("HUF"))

/**
 * This number as forint, so `123.45.huf` is forint at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.huf: Money
    get() = Money.of(this, Currency.of("HUF"))

/**
 * This number as forint, so `"123.45".huf` is forint at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.huf: Money
    get() = Money.of(this, Currency.of("HUF"))

/**
 * This number as forint, so `"123.45".toBigDecimal().huf` is forint at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.huf: Money
    get() = Money.of(this, Currency.of("HUF"))

/**
 * This number as Indian rupees, so `123.inr` is Indian rupees at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.inr: Money
    get() = Money.of(this, Currency.of("INR"))

/**
 * This number as Indian rupees, so `123L.inr` is Indian rupees at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.inr: Money
    get() = Money.of(this, Currency.of("INR"))

/**
 * This number as Indian rupees, so `123.45.inr` is Indian rupees at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.inr: Money
    get() = Money.of(this, Currency.of("INR"))

/**
 * This number as Indian rupees, so `"123.45".inr` is Indian rupees at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.inr: Money
    get() = Money.of(this, Currency.of("INR"))

/**
 * This number as Indian rupees, so `"123.45".toBigDecimal().inr` is Indian rupees at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.inr: Money
    get() = Money.of(this, Currency.of("INR"))

/**
 * This number as Brazilian reais, so `123.brl` is Brazilian reais at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.brl: Money
    get() = Money.of(this, Currency.of("BRL"))

/**
 * This number as Brazilian reais, so `123L.brl` is Brazilian reais at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.brl: Money
    get() = Money.of(this, Currency.of("BRL"))

/**
 * This number as Brazilian reais, so `123.45.brl` is Brazilian reais at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.brl: Money
    get() = Money.of(this, Currency.of("BRL"))

/**
 * This number as Brazilian reais, so `"123.45".brl` is Brazilian reais at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.brl: Money
    get() = Money.of(this, Currency.of("BRL"))

/**
 * This number as Brazilian reais, so `"123.45".toBigDecimal().brl` is Brazilian reais at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.brl: Money
    get() = Money.of(this, Currency.of("BRL"))

/**
 * This number as Mexican pesos, so `123.mxn` is Mexican pesos at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.mxn: Money
    get() = Money.of(this, Currency.of("MXN"))

/**
 * This number as Mexican pesos, so `123L.mxn` is Mexican pesos at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.mxn: Money
    get() = Money.of(this, Currency.of("MXN"))

/**
 * This number as Mexican pesos, so `123.45.mxn` is Mexican pesos at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.mxn: Money
    get() = Money.of(this, Currency.of("MXN"))

/**
 * This number as Mexican pesos, so `"123.45".mxn` is Mexican pesos at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.mxn: Money
    get() = Money.of(this, Currency.of("MXN"))

/**
 * This number as Mexican pesos, so `"123.45".toBigDecimal().mxn` is Mexican pesos at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.mxn: Money
    get() = Money.of(this, Currency.of("MXN"))

/**
 * This number as South African rand, so `123.zar` is South African rand at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.zar: Money
    get() = Money.of(this, Currency.of("ZAR"))

/**
 * This number as South African rand, so `123L.zar` is South African rand at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.zar: Money
    get() = Money.of(this, Currency.of("ZAR"))

/**
 * This number as South African rand, so `123.45.zar` is South African rand at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.zar: Money
    get() = Money.of(this, Currency.of("ZAR"))

/**
 * This number as South African rand, so `"123.45".zar` is South African rand at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.zar: Money
    get() = Money.of(this, Currency.of("ZAR"))

/**
 * This number as South African rand, so `"123.45".toBigDecimal().zar` is South African rand at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.zar: Money
    get() = Money.of(this, Currency.of("ZAR"))

/**
 * This number as won, so `123.krw` is won at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.krw: Money
    get() = Money.of(this, Currency.of("KRW"))

/**
 * This number as won, so `123L.krw` is won at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.krw: Money
    get() = Money.of(this, Currency.of("KRW"))

/**
 * This number as won, so `123.45.krw` is won at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.krw: Money
    get() = Money.of(this, Currency.of("KRW"))

/**
 * This number as won, so `"123.45".krw` is won at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.krw: Money
    get() = Money.of(this, Currency.of("KRW"))

/**
 * This number as won, so `"123.45".toBigDecimal().krw` is won at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.krw: Money
    get() = Money.of(this, Currency.of("KRW"))

/**
 * This number as Russian roubles, so `123.rub` is Russian roubles at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.rub: Money
    get() = Money.of(this, Currency.of("RUB"))

/**
 * This number as Russian roubles, so `123L.rub` is Russian roubles at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.rub: Money
    get() = Money.of(this, Currency.of("RUB"))

/**
 * This number as Russian roubles, so `123.45.rub` is Russian roubles at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.rub: Money
    get() = Money.of(this, Currency.of("RUB"))

/**
 * This number as Russian roubles, so `"123.45".rub` is Russian roubles at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.rub: Money
    get() = Money.of(this, Currency.of("RUB"))

/**
 * This number as Russian roubles, so `"123.45".toBigDecimal().rub` is Russian roubles at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.rub: Money
    get() = Money.of(this, Currency.of("RUB"))

/**
 * This number as Israeli new shekels, so `123.ils` is Israeli new shekels at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.ils: Money
    get() = Money.of(this, Currency.of("ILS"))

/**
 * This number as Israeli new shekels, so `123L.ils` is Israeli new shekels at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.ils: Money
    get() = Money.of(this, Currency.of("ILS"))

/**
 * This number as Israeli new shekels, so `123.45.ils` is Israeli new shekels at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.ils: Money
    get() = Money.of(this, Currency.of("ILS"))

/**
 * This number as Israeli new shekels, so `"123.45".ils` is Israeli new shekels at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.ils: Money
    get() = Money.of(this, Currency.of("ILS"))

/**
 * This number as Israeli new shekels, so `"123.45".toBigDecimal().ils` is Israeli new shekels at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.ils: Money
    get() = Money.of(this, Currency.of("ILS"))

/**
 * This number as bitcoin, so `123.btc` is bitcoin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.btc: Money
    get() = Money.of(this, Currency.of("BTC"))

/**
 * This number as bitcoin, so `123L.btc` is bitcoin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.btc: Money
    get() = Money.of(this, Currency.of("BTC"))

/**
 * This number as bitcoin, so `123.45.btc` is bitcoin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.btc: Money
    get() = Money.of(this, Currency.of("BTC"))

/**
 * This number as bitcoin, so `"123.45".btc` is bitcoin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.btc: Money
    get() = Money.of(this, Currency.of("BTC"))

/**
 * This number as bitcoin, so `"123.45".toBigDecimal().btc` is bitcoin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.btc: Money
    get() = Money.of(this, Currency.of("BTC"))

/**
 * This number as ether, so `123.eth` is ether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.eth: Money
    get() = Money.of(this, Currency.of("ETH"))

/**
 * This number as ether, so `123L.eth` is ether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.eth: Money
    get() = Money.of(this, Currency.of("ETH"))

/**
 * This number as ether, so `123.45.eth` is ether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.eth: Money
    get() = Money.of(this, Currency.of("ETH"))

/**
 * This number as ether, so `"123.45".eth` is ether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.eth: Money
    get() = Money.of(this, Currency.of("ETH"))

/**
 * This number as ether, so `"123.45".toBigDecimal().eth` is ether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.eth: Money
    get() = Money.of(this, Currency.of("ETH"))

/**
 * This number as tether, so `123.usdt` is tether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.usdt: Money
    get() = Money.of(this, Currency.of("USDT"))

/**
 * This number as tether, so `123L.usdt` is tether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.usdt: Money
    get() = Money.of(this, Currency.of("USDT"))

/**
 * This number as tether, so `123.45.usdt` is tether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.usdt: Money
    get() = Money.of(this, Currency.of("USDT"))

/**
 * This number as tether, so `"123.45".usdt` is tether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.usdt: Money
    get() = Money.of(this, Currency.of("USDT"))

/**
 * This number as tether, so `"123.45".toBigDecimal().usdt` is tether at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.usdt: Money
    get() = Money.of(this, Currency.of("USDT"))

/**
 * This number as USD coin, so `123.usdc` is USD coin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Int.usdc: Money
    get() = Money.of(this, Currency.of("USDC"))

/**
 * This number as USD coin, so `123L.usdc` is USD coin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val Long.usdc: Money
    get() = Money.of(this, Currency.of("USDC"))

/**
 * This number as USD coin, so `123.45.usdc` is USD coin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws IllegalArgumentException if this is `NaN` or infinite.
 */
public val Double.usdc: Money
    get() = Money.of(this, Currency.of("USDC"))

/**
 * This number as USD coin, so `"123.45".usdc` is USD coin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 *
 * @throws NumberFormatException if this is not a valid decimal number.
 */
public val String.usdc: Money
    get() = Money.of(this, Currency.of("USDC"))

/**
 * This number as USD coin, so `"123.45".toBigDecimal().usdc` is USD coin at the currency's scale.
 *
 * The literal spelling of [Money.of].
 */
public val BigDecimal.usdc: Money
    get() = Money.of(this, Currency.of("USDC"))
