package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable

@Serializable
data class StepData(
    var action: String? = null,
    var expectedResult: String? = null,
    var inputData: String? = null,
    var attachments: MutableList<String> = mutableListOf()
)
