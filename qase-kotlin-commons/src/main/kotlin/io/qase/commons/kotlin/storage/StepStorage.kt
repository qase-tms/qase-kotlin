package io.qase.commons.kotlin.storage

import io.qase.commons.kotlin.models.StepResult
import java.util.*
import java.util.concurrent.ConcurrentHashMap

object StepStorage {
    private val STEP_IN_PROGRESS = ThreadLocal<StepResult>()
    private val STEP_ID = ThreadLocal<String>()
    private val STEPS_STORAGE = ThreadLocal.withInitial { LinkedList<StepResult>() }
    private val STEPS_MAP = ConcurrentHashMap<String, StepResult>()

    fun startStep() {
        val resultCreateSteps = StepResult()

        if (isStepInProgress()) {
            val currentStep = getCurrentStep()
            resultCreateSteps.parentId = currentStep.id
            currentStep.steps.add(resultCreateSteps)
        }

        STEP_ID.set(resultCreateSteps.id)
        STEP_IN_PROGRESS.set(resultCreateSteps)
        STEPS_MAP[resultCreateSteps.id] = resultCreateSteps
    }

    fun stopStep() {
        checkStepIsInProgress()

        val resultCreateSteps = STEP_IN_PROGRESS.get() ?: return
        resultCreateSteps.execution.stop()

        if (resultCreateSteps.parentId != null) {
            STEP_ID.set(resultCreateSteps.parentId)
            STEP_IN_PROGRESS.set(STEPS_MAP[resultCreateSteps.parentId])
        } else {
            STEP_IN_PROGRESS.remove()
            STEPS_STORAGE.get().add(resultCreateSteps)
        }
    }

    fun getCurrentStep(): StepResult {
        checkStepIsInProgress()
        return STEP_IN_PROGRESS.get() ?: throw IllegalStateException("No step in progress.")
    }

    fun isStepInProgress(): Boolean {
        return STEP_IN_PROGRESS.get() != null
    }

    private fun checkStepIsInProgress() {
        if (!isStepInProgress()) {
            throw IllegalStateException("A step has not been started yet.")
        }
    }

    private fun checkStepIsNotInProgress() {
        if (isStepInProgress()) {
            throw IllegalStateException("A previous step is still in progress.")
        }
    }

    fun stopSteps(): LinkedList<StepResult> {
        checkStepIsNotInProgress()
        val resultCreateSteps = STEPS_STORAGE.get()
        STEPS_STORAGE.remove()
        return resultCreateSteps
    }
}
