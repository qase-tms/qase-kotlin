package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Attachment (
    val id: String = UUID.randomUUID().toString(),
    val fileName: String? = null,
    val mimeType: String? = null,
    val content: String? = null,
    val filePath: String? = null
)
