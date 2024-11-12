package io.qase.commons.kotlin.writer

import io.qase.commons.kotlin.models.Attachment
import io.qase.commons.kotlin.models.TestResult

interface Writer {
    fun writeResult(testResult: TestResult)
    fun writeAttachment(attachment: Attachment)
}
