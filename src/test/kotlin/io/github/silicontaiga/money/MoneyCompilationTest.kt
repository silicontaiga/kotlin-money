// kctfork drives the compiler through its own experimental API. Opting in here, at the one file that
// touches it, keeps `allWarningsAsErrors` switched on for every other line of the project.
@file:OptIn(ExperimentalCompilerApi::class)

package io.github.silicontaiga.money

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import java.io.OutputStream

/**
 * The API's negative space: spellings that must *not* compile.
 *
 * These guarantees cannot be pinned any other way. The committed API dump proves an absent
 * declaration by absence, but nothing else in the build would notice `Currency`'s constructor
 * becoming public or the class losing `final` — and with either, a code could come to mean two
 * currencies with different scales, so two money values could be `==` and still disagree under
 * `withCurrencyScale`.
 *
 * Every rejection is paired with the accepted spelling it differs from, so a snippet that failed
 * for some unrelated reason — a typo, a missing import — shows up as the positive case going red
 * rather than as a rejection passing for the wrong reason.
 */
class MoneyCompilationTest : FunSpec({

    test("a currency is reached through the registry") {
        expression("""Currency.of("EUR")""").exitCode shouldBe KotlinCompilation.ExitCode.OK
    }

    test("a currency cannot be constructed, so one code cannot mean two currencies") {
        expression("""Currency("MONOPOLY", 0, "M")""").exitCode shouldBe
            KotlinCompilation.ExitCode.COMPILATION_ERROR
    }

    test("a currency cannot be subclassed") {
        declaration("""class Fake : Currency("X", 0, "X")""").exitCode shouldBe
            KotlinCompilation.ExitCode.COMPILATION_ERROR
    }

    test("money is reached through its factory") {
        expression("""Money.of(1, Currency.of("EUR"))""").exitCode shouldBe KotlinCompilation.ExitCode.OK
    }

    test("money cannot be constructed directly, so it cannot bypass the scale rules") {
        expression("""Money(BigDecimal.ONE, Currency.of("EUR"))""").exitCode shouldBe
            KotlinCompilation.ExitCode.COMPILATION_ERROR
    }

    test("money cannot be subclassed") {
        declaration("""class Fake : Money(BigDecimal.ONE, Currency.of("EUR"))""").exitCode shouldBe
            KotlinCompilation.ExitCode.COMPILATION_ERROR
    }

    test("a currency mismatch is caught, not raised, by callers") {
        declaration(
            """
            fun snippet() {
                try {
                    1.eur + 1.usd
                } catch (expected: MismatchedCurrencyException) {
                    println(expected.first)
                    println(expected.second)
                }
            }
            """.trimIndent(),
        ).exitCode shouldBe KotlinCompilation.ExitCode.OK
    }

    test("a currency mismatch cannot be raised by callers") {
        expression("""MismatchedCurrencyException(Currency.of("EUR"), Currency.of("USD"))""")
            .exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
    }

    test("currency changes are declared inside a configure block") {
        declaration(
            """
            fun snippet() {
                Money.configure { addCurrency(code = "MONOPOLY", scale = 0, symbol = "M") }
            }
            """.trimIndent(),
        ).exitCode shouldBe KotlinCompilation.ExitCode.OK
    }

    test("a configuration cannot be built outside a configure block") {
        expression("CurrencyConfiguration(emptyMap())").exitCode shouldBe
            KotlinCompilation.ExitCode.COMPILATION_ERROR
    }

    test("ratios are allocated as a list") {
        expression("10.eur.allocate(listOf(1, 1, 3))").exitCode shouldBe KotlinCompilation.ExitCode.OK
    }

    test("ratios cannot be spelled as loose arguments, which would have read as parts and scale") {
        expression("10.eur.allocate(1, 1, 3)").exitCode shouldBe KotlinCompilation.ExitCode.COMPILATION_ERROR
    }
})

/** Compiles [expression] against this library, returning the compiler's verdict. */
private fun expression(expression: String) = declaration("fun snippet(): Any? = $expression")

/** Compiles [code] against this library, returning the compiler's verdict. */
private fun declaration(code: String) =
    KotlinCompilation()
        .apply {
            sources =
                listOf(
                    SourceFile.kotlin(
                        "Snippet.kt",
                        """
                        import io.github.silicontaiga.money.*
                        import io.github.silicontaiga.percentage.*
                        import java.math.BigDecimal

                        $code
                        """.trimIndent(),
                    ),
                )
            inheritClassPath = true
            messageOutputStream = OutputStream.nullOutputStream()
        }.compile()
