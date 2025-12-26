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
        // Clean up previous test case if it wasn't properly finished
        if (CasesStorage.isCaseInProgress()) {
            try {
                val previousCase = CasesStorage.getCurrentCase()
                // Stop any remaining steps first
                try {
                    StepStorage.stopSteps()
                } catch (e: Exception) {
                    // Ignore
                }
                // Always stop the case, even if writing fails
                CasesStorage.stopCase()
                // Write the previous result as failed (orphaned test)
                try {
                    previousCase.execution.apply {
                        status = TestResultStatus.FAILED
                        val endTimeValue = System.currentTimeMillis()
                        endTime = endTimeValue
                        duration = if (startTime != null) ((endTimeValue - startTime!!).toInt()) else 0
                    }
                    writer.writeResult(previousCase)
                } catch (e: Exception) {
                    // Ignore write errors - case is already stopped
                }
            } catch (e: Exception) {
                // If cleanup fails, force stop the case
                try {
                    if (CasesStorage.isCaseInProgress()) {
                        CasesStorage.stopCase()
                    }
                } catch (e2: Exception) {
                    // Ignore
                }
            }
        }
        val resultCreate = startTestCase(description)
        CasesStorage.startCase(resultCreate)
    }

    override fun testFinished(description: Description) {
        // Only process if test case is still in progress (not already stopped by testFailure)
        if (addIfNotPresent(description) && CasesStorage.isCaseInProgress()) {
            val result = stopTestCase(TestResultStatus.PASSED, null)
            if (result != null) {
                writer.writeResult(result)
            }
        }
    }

    override fun testFailure(failure: Failure) {
        if (addIfNotPresent(failure.description)) {
            // Ensure test case is started (might not be if failure occurs before @Test method)
            if (!CasesStorage.isCaseInProgress()) {
                val resultCreate = startTestCase(failure.description)
                CasesStorage.startCase(resultCreate)
            }
            val result = stopTestCase(TestResultStatus.FAILED, failure.exception)
            if (result != null) {
                writer.writeResult(result)
            }
        }
    }

    override fun testAssumptionFailure(failure: Failure) {
        if (addIfNotPresent(failure.description)) {
            // Ensure test case is started (might not be if failure occurs before @Test method)
            if (!CasesStorage.isCaseInProgress()) {
                val resultCreate = startTestCase(failure.description)
                CasesStorage.startCase(resultCreate)
            }
            val result = stopTestCase(TestResultStatus.FAILED, failure.exception)
            if (result != null) {
                writer.writeResult(result)
            }
        }
    }

    override fun testIgnored(description: Description) {
        if (addIfNotPresent(description)) {
            // Ensure test case is started for ignored tests
            if (!CasesStorage.isCaseInProgress()) {
                val resultCreate = startTestCase(description)
                CasesStorage.startCase(resultCreate)
            }
            val result = stopTestCase(TestResultStatus.SKIPPED, null)
            if (result != null) {
                writer.writeResult(result)
            }
        }
    }

    fun addAttachments(vararg attachments: Attachment) {
        if (!CasesStorage.isCaseInProgress() && !StepStorage.isStepInProgress()) {
            return
        }
        
        val processedAttachments = attachments.map { attachment ->
            val path = writer.writeAttachment(attachment)
            attachment.copy(filePath = path)
        }

        if (StepStorage.isStepInProgress()) {
            StepStorage.getCurrentStep().execution.attachments.addAll(processedAttachments)
        } else if (CasesStorage.isCaseInProgress()) {
            CasesStorage.getCurrentCase().attachments.addAll(processedAttachments)
        }
    }

    fun addIdToCurrentCase(id: Long) {
        if (CasesStorage.isCaseInProgress()) {
            val currentCase = CasesStorage.getCurrentCase()
            // Add to testops_ids list
            if (!currentCase.testopsIds.contains(id)) {
                currentCase.testopsIds.add(id)
            }
        }
    }

    fun addIdsToCurrentCase(ids: List<Long>) {
        if (CasesStorage.isCaseInProgress() && ids.isNotEmpty()) {
            val currentCase = CasesStorage.getCurrentCase()
            // Add all ids to testops_ids list
            ids.forEach { id ->
                if (!currentCase.testopsIds.contains(id)) {
                    currentCase.testopsIds.add(id)
                }
            }
        }
    }

    fun addCommentToCurrentCase(comment: String) {
        if (CasesStorage.isCaseInProgress()) {
            CasesStorage.getCurrentCase().message = comment
        }
    }

    fun addTitleToCurrentCase(title: String) {
        if (CasesStorage.isCaseInProgress()) {
            CasesStorage.getCurrentCase().title = title
        }
    }

    fun addFieldsToCurrentCase(fields: Map<String, String>) {
        if (CasesStorage.isCaseInProgress()) {
            CasesStorage.getCurrentCase().fields.putAll(fields)
        }
    }

    fun addParametersToCurrentCase(params: Map<String, String>) {
        if (CasesStorage.isCaseInProgress()) {
            CasesStorage.getCurrentCase().params.putAll(params)
        }
    }

    fun addIgnoreToCurrentCase() {
        if (CasesStorage.isCaseInProgress()) {
            CasesStorage.getCurrentCase().ignore = true
        }
    }

    private fun addIfNotPresent(description: Description): Boolean {
        val methodName = description.methodName ?: "unknown"
        val methodFullName = description.className + methodName
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
        resultCreate.testopsIds = mutableListOf()
        resultCreate.title = description.methodName ?: description.className
        resultCreate.fields = mutableMapOf()
        resultCreate.relations = Relations()
        resultCreate.signature = generateSignature(description, null, null)

        // Build suite hierarchy from class name path
        // e.g., com.example.simpleapp.MainActivityTest -> [com, example, simpleapp, mainactivitytest]
        val className = description.className
        val suiteParts = className.split(".").map { it.lowercase() }
        resultCreate.relations.suite.data = suiteParts.map { part ->
            io.qase.commons.kotlin.models.SuiteData(
                title = part,
                publicId = null
            )
        }.toMutableList()

        // Process annotations from qase-kotlin-junit4 module
        processAnnotations(description, resultCreate)

        return resultCreate
    }

    private fun processAnnotations(description: Description, result: TestResult) {
        val methodName = description.methodName ?: return
        val className = description.className

        try {
            val testClass = Class.forName(className)
            val method = testClass.declaredMethods.find { it.name == methodName }
                ?: return

            // Process @QaseId
            try {
                val qaseIdClass = Class.forName("io.qase.commons.kotlin.junit4.QaseId")
                val qaseIdAnnotation = method.getAnnotation(qaseIdClass as Class<out Annotation>)
                if (qaseIdAnnotation != null) {
                    val valueMethod = qaseIdClass.getMethod("value")
                    val id = valueMethod.invoke(qaseIdAnnotation) as Long
                    result.testopsIds.add(id)
                }
            } catch (e: Exception) {
                // Annotation class not available, skip
            }

            // Process @QaseIds
            try {
                val qaseIdsClass = Class.forName("io.qase.commons.kotlin.junit4.QaseIds")
                val qaseIdsAnnotation = method.getAnnotation(qaseIdsClass as Class<out Annotation>)
                if (qaseIdsAnnotation != null) {
                    val valueMethod = qaseIdsClass.getMethod("value")
                    val ids = valueMethod.invoke(qaseIdsAnnotation) as LongArray
                    ids.forEach { id ->
                        if (!result.testopsIds.contains(id)) {
                            result.testopsIds.add(id)
                        }
                    }
                }
            } catch (e: Exception) {
                // Annotation class not available, skip
            }

            // Process @QaseTitle
            try {
                val qaseTitleClass = Class.forName("io.qase.commons.kotlin.junit4.QaseTitle")
                val qaseTitleAnnotation = method.getAnnotation(qaseTitleClass as Class<out Annotation>)
                if (qaseTitleAnnotation != null) {
                    val valueMethod = qaseTitleClass.getMethod("value")
                    val title = valueMethod.invoke(qaseTitleAnnotation) as String
                    result.title = title
                }
            } catch (e: Exception) {
                // Annotation class not available, skip
            }

            // Process @QaseIgnore
            try {
                val qaseIgnoreClass = Class.forName("io.qase.commons.kotlin.junit4.QaseIgnore")
                val qaseIgnoreAnnotation = method.getAnnotation(qaseIgnoreClass as Class<out Annotation>)
                if (qaseIgnoreAnnotation != null) {
                    result.ignore = true
                }
            } catch (e: Exception) {
                // Annotation class not available, skip
            }

            // Process @QaseField (repeatable)
            try {
                val qaseFieldClass = Class.forName("io.qase.commons.kotlin.junit4.QaseField")
                val keyMethod = qaseFieldClass.getMethod("key")
                val valueMethod = qaseFieldClass.getMethod("value")
                // Check for multiple @QaseField annotations
                val annotations = method.getAnnotationsByType(qaseFieldClass as Class<out Annotation>)
                annotations.forEach { annotation ->
                    val key = keyMethod.invoke(annotation) as String
                    val value = valueMethod.invoke(annotation) as String
                    result.fields[key] = value
                }
            } catch (e: Exception) {
                // Annotation class not available, skip
            }

            // Process @QaseParameter (repeatable)
            try {
                val qaseParameterClass = Class.forName("io.qase.commons.kotlin.junit4.QaseParameter")
                val keyMethod = qaseParameterClass.getMethod("key")
                val valueMethod = qaseParameterClass.getMethod("value")
                // Check for multiple @QaseParameter annotations
                val annotations = method.getAnnotationsByType(qaseParameterClass as Class<out Annotation>)
                annotations.forEach { annotation ->
                    val key = keyMethod.invoke(annotation) as String
                    val value = valueMethod.invoke(annotation) as String
                    result.params[key] = value
                }
            } catch (e: Exception) {
                // Annotation class not available, skip
            }
        } catch (e: Exception) {
            // Class not found or reflection failed, skip annotation processing
        }
    }
    

    private fun stopTestCase(status: TestResultStatus, error: Throwable?): TestResult? {
        val resultCreate = CasesStorage.getCurrentCase()
        CasesStorage.stopCase()

        if (resultCreate.ignore) {
            return null
        }

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
        val methodName = description.methodName?.split(".")?.last()?.lowercase() ?: "unknown"
        val qaseIdPart = qaseId?.let { "::$it" } ?: ""
        val parametersPart = parameters?.entries?.joinToString("::") {
            "${it.key.lowercase()}::${it.value.lowercase().replace(" ", "_")}"
        } ?: ""

        return "$className::$methodName$qaseIdPart$parametersPart"
    }
}

