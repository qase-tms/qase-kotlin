package io.qase.commons.kotlin.models

data class StepExecution(
    var status: StepResultStatus = StepResultStatus.UNTESTED,
    var startTime: Long = System.currentTimeMillis(),
    var endTime: Long = 0,
    var duration: Int = 0
) {
    fun stop() {
        endTime = System.currentTimeMillis()
        duration = (endTime - startTime).toInt()
    }
}
