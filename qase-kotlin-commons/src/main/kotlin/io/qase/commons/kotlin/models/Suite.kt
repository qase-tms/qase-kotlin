package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable


@Serializable
data class Suite(
    val data: MutableList<SuiteData> = mutableListOf(),
)
