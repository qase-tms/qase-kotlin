package io.qase.commons.kotlin

import io.qase.commons.kotlin.models.StepResultStatus
import io.qase.commons.kotlin.storage.StepStorage

/**
 * Executes a block of code as a step in test results.
 * This is similar to Allure.step() functionality.
 *
 * Usage:
 * ```
 * step("Perform action") {
 *     // Step implementation
 * }
 * ```
 *
 * @param stepName Name of the step to be displayed in test results
 * @param block Code block to execute as a step
 * @return Result of the block execution
 */
inline fun <T> step(stepName: String, block: () -> T): T {
    StepStorage.startStep()
    val currentStep = StepStorage.getCurrentStep()
    currentStep.data.action = stepName

    try {
        val result = block()
        currentStep.execution.status = StepResultStatus.PASSED
        return result
    } catch (e: Throwable) {
        currentStep.execution.status = StepResultStatus.FAILED
        throw e
    } finally {
        StepStorage.stopStep()
    }
}

