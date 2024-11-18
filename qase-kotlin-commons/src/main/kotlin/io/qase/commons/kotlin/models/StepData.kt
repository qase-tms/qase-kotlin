package io.qase.commons.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StepData(
    var action: String? = null,
    @SerialName("expected_result")
    var expectedResult: String? = null,
    var inputData: String? = null,
)
