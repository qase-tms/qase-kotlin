package io.qase.commons.kotlin.junit4

import io.qase.commons.kotlin.listener.Qase

/**
 * Helper object for interacting with Qase test reporting in JUnit4 tests.
 *
 * Usage:
 * ```kotlin
 * @Test
 * fun myTest() {
 *     Qase.comment("Test comment")
 *     // ... test code
 * }
 * ```
 */
object Qase {
    @JvmStatic
    fun comment(comment: String) {
        Qase.listener?.addCommentToCurrentCase(comment)
    }
}

