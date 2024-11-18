package io.qase.commons.kotlin.listener

import io.qase.commons.kotlin.models.Attachment
import io.qase.commons.kotlin.models.Relations
import io.qase.commons.kotlin.models.TestResult
import io.qase.commons.kotlin.models.TestResultStatus
import io.qase.commons.kotlin.storage.CasesStorage
import io.qase.commons.kotlin.storage.StepStorage
import io.qase.commons.kotlin.writer.FileWriter
import io.qase.commons.kotlin.writer.Writer
import org.junit.runner.Description
import org.junit.runner.notification.RunListener
import org.junit.runner.notification.Failure
import java.io.PrintWriter
import java.io.StringWriter


class QaseJunit4Listener(private val writer: Writer = FileWriter("qase-results")) : RunListener() {
    private val methods = mutableSetOf<String>()

    override fun testStarted(description: Description) {
        val resultCreate = startTestCase(description)
        CasesStorage.startCase(resultCreate)
    }

    override fun testFinished(description: Description) {
        if (addIfNotPresent(description)) {
            val result = stopTestCase(TestResultStatus.PASSED, null)
            if (result != null) {
                writer.writeResult(result)
            }
        }
    }

    override fun testFailure(failure: Failure) {
        if (addIfNotPresent(failure.description)) {
            val result = stopTestCase(TestResultStatus.FAILED, failure.exception)
            if (result != null) {
                writer.writeResult(result)
            }
        }
    }

    override fun testAssumptionFailure(failure: Failure) {
        if (addIfNotPresent(failure.description)) {
            val result = stopTestCase(TestResultStatus.FAILED, failure.exception)
            if (result != null) {
                writer.writeResult(result)
            }
        }
    }

    override fun testIgnored(description: Description) {
        if (addIfNotPresent(description)) {
            val result = stopTestCase(TestResultStatus.SKIPPED, null)
            if (result != null) {
                writer.writeResult(result)
            }
        }
    }

    fun addAttachments(vararg attachments: Attachment) {
        val processedAttachments = attachments.map { attachment ->
            val path = writer.writeAttachment(attachment)
            attachment.copy(filePath = path)
        }

        if (StepStorage.isStepInProgress()) {
            StepStorage.getCurrentStep().execution.attachments.addAll(processedAttachments)
        } else {
            CasesStorage.getCurrentCase().attachments.addAll(processedAttachments)
        }
    }

    fun addIdToCurrentCase(id: Long) {
        CasesStorage.getCurrentCase().testopsId = id
    }

    fun addCommentToCurrentCase(comment: String) {
        CasesStorage.getCurrentCase().message = comment
    }

    fun addTitleToCurrentCase(title: String) {
        CasesStorage.getCurrentCase().title = title
    }

    fun addFieldsToCurrentCase(fields: Map<String, String>) {
        CasesStorage.getCurrentCase().fields.putAll(fields)
    }

    fun addParametersToCurrentCase(params: Map<String, String>) {
        CasesStorage.getCurrentCase().params.putAll(params)
    }

    private fun addIfNotPresent(description: Description): Boolean {
        val methodFullName = description.className + description.methodName
        return if (methods.contains(methodFullName)) {
            false
        } else {
            methods.add(methodFullName)
            true
        }
    }

    private fun startTestCase(description: Description): TestResult {
        val resultCreate = TestResult()

        resultCreate.execution.startTime = System.currentTimeMillis()
        resultCreate.testopsId = null
        resultCreate.title = description.methodName
        resultCreate.fields = mutableMapOf()
        resultCreate.relations = Relations()
        resultCreate.signature = generateSignature(description, null, null)

        return resultCreate
    }

    private fun stopTestCase(status: TestResultStatus, error: Throwable?): TestResult? {
        val resultCreate = CasesStorage.getCurrentCase()
        CasesStorage.stopCase()

        val comment = error?.toString()
        val stacktrace = error?.let { getStacktrace(it) }
        val steps = StepStorage.stopSteps()

        resultCreate.execution.apply {
            this.status = status
            endTime = System.currentTimeMillis()
            duration = (endTime!! - startTime!!).toInt()
            this.stacktrace = stacktrace
        }
        resultCreate.steps = steps
        resultCreate.message = comment?.let { resultCreate.message?.plus("\n\n$it") ?: it }

        return resultCreate
    }

    private fun getStacktrace(throwable: Throwable): String {
        val stringWriter = StringWriter()
        throwable.printStackTrace(PrintWriter(stringWriter))
        return stringWriter.toString()
    }

    private fun generateSignature(description: Description, qaseId: Long?, parameters: Map<String, String>?): String {
        val className = description.className.lowercase().replace(".", ":")
        val methodName = description.methodName.split(".").last().lowercase()
        val qaseIdPart = qaseId?.let { "::$it" } ?: ""
        val parametersPart = parameters?.entries?.joinToString("::") {
            "${it.key.lowercase()}::${it.value.lowercase().replace(" ", "_")}"
        } ?: ""

        return "$className.java::$className::$methodName$qaseIdPart$parametersPart"
    }
}

