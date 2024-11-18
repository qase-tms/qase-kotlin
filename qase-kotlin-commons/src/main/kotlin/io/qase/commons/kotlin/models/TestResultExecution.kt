package io.qase.commons.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestResultExecution(
    @SerialName("start_time")
    var startTime: Long? = null,
    var status: TestResultStatus? = null,
    @SerialName("end_time")
    var endTime: Long? = null,
    var duration: Int? = null,
    var stacktrace: String? = null,
    var thread: String? = null
)
