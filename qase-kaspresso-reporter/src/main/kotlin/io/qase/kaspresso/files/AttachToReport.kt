package io.qase.kaspresso.files

import io.qase.commons.kotlin.listener.Qase
import io.qase.commons.kotlin.models.Attachment
import java.io.File


fun File.attachScreenshotToReport(): Unit {
    val attachment = Attachment()
    attachment.filePath = this.path
    attachment.mimeType = "image/png"

    Qase.listener?.addAttachments(attachment)
}

fun File.attachLogcatToReport(): Unit {
    val attachment = Attachment()
    attachment.filePath = this.path
    attachment.mimeType = "text/plain"

    Qase.listener?.addAttachments(attachment)
}

fun File.attachViewHierarchyToReport(): Unit {
    val attachment = Attachment()
    attachment.filePath = this.path
    attachment.mimeType = "text/xml"

    Qase.listener?.addAttachments(attachment)
}
