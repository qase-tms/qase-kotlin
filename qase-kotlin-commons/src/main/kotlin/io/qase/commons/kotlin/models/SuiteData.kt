package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable

@Serializable
data class SuiteData(
    var title: String = "",
    var publicId: Int? = null,
)
