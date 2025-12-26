package io.qase.commons.kotlin.android.writer

import android.annotation.SuppressLint
import androidx.test.services.storage.TestStorage
import io.qase.commons.kotlin.models.Attachment
import io.qase.commons.kotlin.models.TestResult
import io.qase.commons.kotlin.writer.Writer
import io.qase.commons.kotlin.writer.OutputStreamWriter

class TestStorageResultsWriter(
    resultsPath: String = "qase-results"
) : Writer {
    @Suppress("RestrictedApi")
    private val testStorage by lazy { 
        try {
            TestStorage()
        } catch (e: Exception) {
            throw IllegalStateException("TestStorage is not available. Make sure you're running on a device with AndroidX Test Services.", e)
        }
    }

    @SuppressLint("RestrictedApi")
    private val outputStreamResultsWriter = OutputStreamWriter(
        streamProvider = { name ->
            try {
                testStorage.openOutputFile("$resultsPath/$name")
            } catch (e: Exception) {
                throw IllegalStateException("Failed to open output file: $resultsPath/$name. TestStorage may not be available.", e)
            }
        }
    )

    override fun writeResult(testResult: TestResult) {
        outputStreamResultsWriter.writeResult(testResult)
    }

    override fun writeAttachment(attachment: Attachment): String {
        return outputStreamResultsWriter.writeAttachment(attachment)
    }
}
