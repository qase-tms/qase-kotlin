package io.qase.commons.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuiteData(
    var title: String = "",
    @SerialName("public_id")
    var publicId: Int? = null,
)
