package io.qase.commons.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class StepResult(
    var id: String = UUID.randomUUID().toString(),
    var data: StepData = StepData(),
    @SerialName("parent_id")
    var parentId: String? = null,
    var execution: StepExecution = StepExecution(),
    var steps: MutableList<StepResult> = mutableListOf()
)
