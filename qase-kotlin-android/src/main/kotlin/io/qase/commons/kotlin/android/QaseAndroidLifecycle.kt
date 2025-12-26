package io.qase.commons.kotlin.android

import androidx.test.platform.app.InstrumentationRegistry
import io.qase.commons.kotlin.android.internal.isDeviceTest
import io.qase.commons.kotlin.android.writer.TestStorageResultsWriter
import io.qase.commons.kotlin.listener.QaseJunit4Listener
import io.qase.commons.kotlin.writer.FileWriter
import io.qase.commons.kotlin.writer.Writer
import java.io.File

/**
 * Qase Android lifecycle that uses TestStorageResultsWriter for device tests
 * or FileWriter for Robolectric tests.
 */
open class QaseAndroidLifecycle(
    writer: Writer? = null
) {
    val listener: QaseJunit4Listener = QaseJunit4Listener(writer ?: createDefaultWriter())

    companion object {
        /**
         * Creates default writer based on test environment.
         * Uses TestStorageResultsWriter for device tests, FileWriter for Robolectric.
         */
        fun createDefaultWriter(
            useMultiProjectFormat: Boolean = false,
            defaultProjectCode: String? = null,
            resultsPath: String = "qase-results"
        ): Writer {
            // Always try TestStorageResultsWriter first on Android
            // It will work on device tests and fail gracefully on Robolectric
            return try {
                if (isDeviceTest()) {
                    TestStorageResultsWriter(
                        resultsPath = resultsPath
                    )
                } else {
                    // For Robolectric or non-Android environments, use FileWriter with absolute path
                    FileWriter(
                        path = obtainResultsDirectory().absolutePath
                    )
                }
            } catch (e: Exception) {
                // If TestStorage is not available or any error occurs,
                // fallback to FileWriter with absolute path
                // This ensures we never use relative paths on Android
                FileWriter(
                    path = obtainResultsDirectory().absolutePath
                )
            }
        }

        /**
         * Obtains results directory as a File reference for Robolectric tests.
         * Uses the target context files directory.
         */
        private fun obtainResultsDirectory(): File {
            return try {
                File(InstrumentationRegistry.getInstrumentation().targetContext.filesDir, "qase-results")
            } catch (e: Exception) {
                // Fallback for non-Android environment
                File(System.getProperty("user.dir") ?: ".", "qase-results")
            }
        }
    }
}


