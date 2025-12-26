package io.qase.commons.kotlin.writer

import io.qase.commons.kotlin.models.Attachment
import io.qase.commons.kotlin.models.TestResult
import kotlinx.serialization.json.Json
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class FileWriter(path: String) : Writer {
    private val resultsPath: String = Paths.get(path, "results").toString()
    private val attachmentsPath: String = Paths.get(path, "attachments").toString()

    private val mapper: Json = Json {
        prettyPrint = true
        useArrayPolymorphism = true
    }

    init {
        this.prepare()
    }

    override fun writeResult(testResult: TestResult) {
        val file = Paths.get(resultsPath, (testResult.id + ".json")).toFile()
        try {
            // Ensure directory exists before writing
            file.parentFile?.mkdirs()
            val json = mapper.encodeToString(TestResult.serializer(), testResult)
            file.writeText(json)
        } catch (e: IOException) {
            throw Exception("Could not write test result", e)
        }
    }

    override fun writeAttachment(attachment: Attachment): String {
        if (attachment.filePath != null) {
            val file = Paths.get(attachmentsPath, attachment.id)
            try {
                // Ensure directory exists before copying
                file.parent?.let { Files.createDirectories(it) }
                Files.copy(Paths.get(attachment.filePath), file)
            } catch (e: IOException) {
                throw Exception("Could not write attachment", e)
            }
        }

        val file = Paths.get(attachmentsPath, attachment.id).toFile()
        try {
            // Ensure directory exists before writing
            file.parentFile?.mkdirs()
            attachment.content?.let { file.writeBytes(it.encodeToByteArray()) }
        } catch (e: IOException) {
            throw Exception("Could not write attachment", e)
        }

        return attachment.id
    }

    private fun prepare() {
        try {
            val resultsFolder = Paths.get(resultsPath)
            Files.createDirectories(resultsFolder)
            val attachmentsFolder = Paths.get(attachmentsPath)
            Files.createDirectories(attachmentsFolder)
        } catch (e: Exception) {
            // Fallback to mkdirs if Files.createDirectories fails
            val resultsFolder = Paths.get(resultsPath).toFile()
            if (!resultsFolder.exists()) {
                resultsFolder.mkdirs()
            }
            val attachmentsFolder = Paths.get(attachmentsPath).toFile()
            if (!attachmentsFolder.exists()) {
                attachmentsFolder.mkdirs()
            }
        }
    }
}
