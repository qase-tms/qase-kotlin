package io.qase.commons.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TestResultStatus {
    @SerialName("passed")
    PASSED,

    @SerialName("failed")
    FAILED,

    @SerialName("skipped")
    SKIPPED
}
