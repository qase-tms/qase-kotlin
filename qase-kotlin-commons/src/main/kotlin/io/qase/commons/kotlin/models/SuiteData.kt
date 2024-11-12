package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable

@Serializable
data class SuiteData(
    val title: String = "",
    val publicId: Int? = null,
)
