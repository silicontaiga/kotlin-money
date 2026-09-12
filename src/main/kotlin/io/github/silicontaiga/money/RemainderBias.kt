package io.github.silicontaiga.money

/**
 * Which parts of an allocation receive what an even division leaves over.
 *
 * An enum rather than a boolean, because `allocate(3, true)` says nothing at a call site, and
 * because a further rule such as largest-remainder could be added later without breaking callers.
 */
public enum class RemainderBias {
    /** Hand the leftovers to the earliest parts, starting with the first. */
    FIRST,

    /** Hand the leftovers to the latest parts, starting with the last. */
    LAST,
}
