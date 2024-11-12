package io.qase.commons.kotlin.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class StepResult(
    var id: String = UUID.randomUUID().toString(),
    var data: StepData = StepData(),
    var parentId: String? = null,
    var execution: StepExecution = StepExecution(),
    @Contextual
    var throwable: Throwable? = null,
    var attachments: MutableList<String> = mutableListOf(),
    var steps: MutableList<StepResult> = mutableListOf()
)
