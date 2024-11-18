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
//        if (getQaseIgnore(description)) {
//            resultCreate.ignore = true
//            return resultCreate
//        }

//        val caseId = getCaseId(description)
//        val caseTitle = getCaseTitle(description)
//        val fields = getQaseFields(description)
//        val suite = getQaseSuite(description)
//        val relations = Relations()

//        suite?.split("\t")?.forEach { part ->
//            val data = SuiteData().apply { title = part }
//            relations.suite.data.add(data)
//        } ?: run {
//            relations.suite.data.add(SuiteData().apply { title = description.className })
//        }

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
//        if (resultCreate.ignore) return null

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

//    private fun getCaseId(description: Description): Long? {
//        return getQaseId(description) ?: description.getAnnotation(CaseId::class.java)?.value()
//    }
//
//    private fun getCaseTitle(description: Description): String {
//        return getQaseTitle(description)
//            ?: description.getAnnotation(CaseTitle::class.java)?.value()
//            ?: description.methodName
//    }
//
//    private fun getQaseId(description: Description): Long? {
//        return description.getAnnotation(QaseId::class.java)?.value()
//    }
//
//    private fun getQaseTitle(description: Description): String? {
//        return description.getAnnotation(QaseTitle::class.java)?.value()
//    }
//
//    private fun getQaseIgnore(description: Description): Boolean {
//        return description.getAnnotation(QaseIgnore::class.java) != null
//    }
//
//    private fun getQaseSuite(description: Description): String? {
//        return description.getAnnotation(QaseSuite::class.java)?.value()
//    }
//
//    private fun getQaseFields(description: Description): Map<String, String> {
//        val fields = mutableMapOf<String, String>()
//        description.getAnnotation(QaseFields::class.java)?.value?.forEach { field ->
//            fields[field.name] = field.value
//        }
//        return fields
//    }

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

