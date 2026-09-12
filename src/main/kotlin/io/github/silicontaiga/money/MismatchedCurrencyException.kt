package io.github.silicontaiga.money

/**
 * Thrown when an operation is asked to combine two amounts denominated in different currencies.
 *
 * Mixing currencies is a programming error rather than a runtime condition every correct caller
 * should have to handle, so this is unchecked and there is no failure type to unwrap: without an
 * exchange rate there is no meaningful answer, and an exchange rate is out of this library's scope.
 *
 * Codes identify currencies uniquely, so naming both is enough to say what went wrong.
 */
public class MismatchedCurrencyException internal constructor(
    /** The currency of the value the operation was applied to. */
    public val first: Currency,
    /** The currency of the value it was asked to combine with. */
    public val second: Currency,
) : RuntimeException("Cannot combine ${first.code} with ${second.code}")
