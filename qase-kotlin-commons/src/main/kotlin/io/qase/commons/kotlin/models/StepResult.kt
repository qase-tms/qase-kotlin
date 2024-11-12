package io.qase.commons.kotlin.models


import java.util.UUID

data class StepResult(
    var id: String = UUID.randomUUID().toString(),
    var data: StepData = StepData(),
    var parentId: String? = null,
    var execution: StepExecution = StepExecution(),
    var throwable: Throwable? = null,
    var attachments: MutableList<Attachment> = mutableListOf(),
    var steps: MutableList<StepResult> = mutableListOf()
)
