package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TestResult(
    var id: String = UUID.randomUUID().toString(),
    var title: String? = null,
    var signature: String? = null,
    var runId: String? = null,
    var testopsId: Long? = null,
    var execution: TestResultExecution = TestResultExecution(),
    var fields: MutableMap<String, String> = mutableMapOf(),
    var attachments: MutableList<String> = mutableListOf(),
    var steps: MutableList<StepResult> = mutableListOf(),
    var params: MutableMap<String, String> = mutableMapOf(),
    var paramGroups: MutableList<List<String>> = mutableListOf(),
    var author: String? = null,
    var relations: Relations = Relations(),
    var muted: Boolean = false,
    var message: String? = null
) {}

