package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable


@Serializable
data class Suite(
    var data: MutableList<SuiteData> = mutableListOf(),
)
