package io.qase.commons.kotlin.storage

import io.qase.commons.kotlin.models.Attachment
import io.qase.commons.kotlin.models.TestResult
import java.util.concurrent.ConcurrentHashMap

object CasesStorage {
    private val Attachments: ConcurrentHashMap<String, Attachment> = ConcurrentHashMap()

    private val CURRENT_CASE = ThreadLocal<TestResult>()

    fun startCase(resultCreate: TestResult) {
        checkCaseIsNotInProgress()
        CURRENT_CASE.set(resultCreate)
    }

    fun stopCase() {
        checkCaseIsInProgress()
        CURRENT_CASE.remove()
    }

    fun getCurrentCase(): TestResult {
        checkCaseIsInProgress()
        return CURRENT_CASE.get() ?: throw IllegalStateException("No case is currently in progress.")
    }

    fun isCaseInProgress(): Boolean {
        return CURRENT_CASE.get() != null
    }

    private fun checkCaseIsInProgress() {
        if (!isCaseInProgress()) {
            throw IllegalStateException("A case has not been started yet.")
        }
    }

    private fun checkCaseIsNotInProgress() {
        if (isCaseInProgress()) {
            throw IllegalStateException("Previous case is still in progress.")
        }
    }
}
