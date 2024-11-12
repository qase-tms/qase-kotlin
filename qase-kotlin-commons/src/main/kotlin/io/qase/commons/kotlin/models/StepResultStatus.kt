package io.qase.commons.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StepResultStatus {
    @SerialName("passed")
    PASSED,

    @SerialName("failed")
    FAILED,

    @SerialName("skipped")
    SKIPPED,

    @SerialName("blocked")
    BLOCKED,

    @SerialName("untested")
    UNTESTED
}
