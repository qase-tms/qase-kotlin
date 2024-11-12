package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable

@Serializable
data class StepData(
    val action: String? = null,
    val expectedResult: String? = null,
    val inputData: String? = null,
    val attachments: MutableList<String> = mutableListOf()
)
