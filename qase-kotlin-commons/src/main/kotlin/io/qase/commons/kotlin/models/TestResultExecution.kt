package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable

@Serializable
data class TestResultExecution(
    var startTime: Long? = null,
    var status: TestResultStatus? = null,
    var endTime: Long? = null,
    var duration: Int? = null,
    var stacktrace: String? = null,
    var thread: String? = null
)
