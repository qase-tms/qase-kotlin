package io.qase.commons.kotlin.writer

import io.qase.commons.kotlin.models.Attachment
import io.qase.commons.kotlin.models.TestResult
import kotlinx.serialization.json.Json

import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths

class OutputStreamWriter(private val streamProvider: (name: String) -> OutputStream) : Writer {

    private val mapper = Json {
        prettyPrint = true
        useArrayPolymorphism = true
    }

    private val mimeTypeExtensions = mapOf(
        "text/plain" to ".txt",
        "text/xml" to ".xml",
        "image/png" to ".png",
        "application/json" to ".json",
        "image/jpeg" to ".jpg",
        "image/gif" to ".gif",
        "image/bmp" to ".bmp",
        "image/tiff" to ".tiff",
        "application/pdf" to ".pdf",
        "application/xml" to ".xml",
        "application/zip" to ".zip",
        "video/mp4" to ".mp4",
        "video/x-matroska" to ".mkv",
        "video/x-msvideo" to ".avi",
        "video/quicktime" to ".mov",
        "video/mpeg" to ".mpeg",
        "video/webm" to ".webm",
        "audio/mpeg" to ".mp3",
        "audio/wav" to ".wav",
        "audio/ogg" to ".ogg",
        "audio/flac" to ".flac"
    )

    override fun writeResult(testResult: TestResult) {
        val testResultName = "results/${testResult.id}.json"
        writeToFile(testResultName) {
            val json = mapper.encodeToString(TestResult.serializer(), testResult)
            json.toByteArray()
        }
    }

    override fun writeAttachment(attachment: Attachment): String {
        val extension = mimeTypeExtensions[attachment.mimeType] ?: ""
        val fileName = "attachments/${attachment.id}$extension"

        writeToFile(fileName) {
            attachment.filePath?.let { path ->
                Files.readAllBytes(Paths.get(path))
            } ?: attachment.content?.encodeToByteArray() ?: throw Exception("No data available to write for attachment")
        }

        return fileName
    }

    private fun writeToFile(fileName: String, dataProvider: () -> ByteArray) {
        try {
            streamProvider(fileName).use { outputStream ->
                outputStream.write(dataProvider())
            }
        } catch (e: IOException) {
            throw Exception("Could not write to $fileName", e)
        }
    }
}
