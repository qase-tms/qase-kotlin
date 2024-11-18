package io.qase.commons.kotlin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Attachment(
    var id: String = UUID.randomUUID().toString(),
    @SerialName("file_name")
    var fileName: String? = null,
    @SerialName("mime_type")
    var mimeType: String? = null,
    var content: String? = null,
    @SerialName("file_path")
    var filePath: String? = null
)
