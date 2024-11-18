package io.qase.commons.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StepExecution(
    var status: StepResultStatus = StepResultStatus.UNTESTED,
    @SerialName("start_time")
    var startTime: Long = System.currentTimeMillis(),
    @SerialName("end_time")
    var endTime: Long = 0,
    var duration: Int = 0,
    var attachments: MutableList<Attachment> = mutableListOf()
) {
    fun stop() {
        endTime = System.currentTimeMillis()
        duration = (endTime - startTime).toInt()
    }
}
