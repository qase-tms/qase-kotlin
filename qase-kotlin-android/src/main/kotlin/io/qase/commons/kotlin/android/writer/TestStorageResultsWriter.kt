package io.qase.commons.kotlin.android.writer

import androidx.test.services.storage.TestStorage
import io.qase.commons.kotlin.models.Attachment
import io.qase.commons.kotlin.models.TestResult
import io.qase.commons.kotlin.writer.Writer
import io.qase.commons.kotlin.writer.OutputStreamWriter

class TestStorageResultsWriter : Writer {
    private val defaultPath = "qase-results"
    private val testStorage by lazy { TestStorage() }

    private val outputStreamResultsWriter = OutputStreamWriter { name ->
        testStorage.openOutputFile("$defaultPath/$name")
    }

    override fun writeResult(testResult: TestResult) {
        outputStreamResultsWriter.writeResult(testResult)
    }

    override fun writeAttachment(attachment: Attachment) {
        outputStreamResultsWriter.writeAttachment(attachment)
    }
}
