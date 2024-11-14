package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Attachment (
    var id: String = UUID.randomUUID().toString(),
    var fileName: String? = null,
    var mimeType: String? = null,
    var content: String? = null,
    var filePath: String? = null
)
