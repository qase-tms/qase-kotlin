package io.qase.commons.kotlin.rules

import io.qase.commons.kotlin.Step
import io.qase.commons.kotlin.listener.Qase
import io.qase.commons.kotlin.models.StepResultStatus
import io.qase.commons.kotlin.storage.StepStorage
import org.junit.rules.MethodRule
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement

/**
 * JUnit Rule that intercepts method calls annotated with @Step
 * and tracks them as separate steps in test results.
 *
 * Usage:
 * ```
 * @get:Rule
 * val stepRule = StepRule()
 *
 * @Test
 * fun testExample() {
 *     performStep()  // This will be tracked if annotated with @Step
 * }
 *
 * @Step("Perform step")
 * private fun performStep() {
 *     // Step implementation
 * }
 * ```
 */
class StepRule : MethodRule {
    override fun apply(base: Statement, method: FrameworkMethod, target: Any): Statement {
        return object : Statement() {
            override fun evaluate() {
                val stepAnnotation = method.getAnnotation(Step::class.java)
                if (stepAnnotation != null) {
                    val stepName = stepAnnotation.value.takeIf { it.isNotEmpty() } 
                        ?: method.name
                    executeWithStep(stepName, base)
                } else {
                    base.evaluate()
                }
            }

            private fun executeWithStep(stepName: String, base: Statement) {
                StepStorage.startStep()
                val currentStep = StepStorage.getCurrentStep()
                currentStep.data.action = stepName

                try {
                    base.evaluate()
                    currentStep.execution.status = StepResultStatus.PASSED
                } catch (e: Throwable) {
                    currentStep.execution.status = StepResultStatus.FAILED
                    throw e
                } finally {
                    StepStorage.stopStep()
                }
            }
        }
    }
}

