package io.qase.commons.kotlin.models

data class StepData(
    val action: String? = null,
    val expectedResult: String? = null,
    val inputData: String? = null,
    val attachments: MutableList<Attachment> = mutableListOf()
)
