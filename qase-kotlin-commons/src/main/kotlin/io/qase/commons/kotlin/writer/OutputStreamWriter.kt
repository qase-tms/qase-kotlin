package io.qase.commons.kotlin.writer

import io.qase.commons.kotlin.models.Attachment
import io.qase.commons.kotlin.models.TestResult
import kotlinx.serialization.json.Json

import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths

class OutputStreamWriter(private val streamProvider: (name: String) -> OutputStream) : Writer {

    private val mapper: Json = Json {
        prettyPrint = true
        useArrayPolymorphism = true
    }

    override fun writeResult(testResult: TestResult) {
        val testResultName = testResult.id + ".json"
        try {
            val json = mapper.encodeToString(TestResult.serializer(), testResult)
            streamProvider(testResultName).use {
                it.write(json.toByteArray())
            }
        } catch (e: IOException) {
            throw Exception("Could not write test result", e)
        }
    }

    override fun writeAttachment(attachment: Attachment) {

        if (attachment.filePath != null) {
            try {
                val input = Files.readAllBytes(Paths.get(attachment.filePath))
                streamProvider(attachment.id).use {
                    it.write(input)
                }
            } catch (e: IOException) {
                throw Exception("Could not write attachment", e)
            }
        }

        try {
            streamProvider(attachment.id).use {
                attachment.content?.encodeToByteArray()?.let { it1 -> it.write(it1) }
            }
        } catch (e: IOException) {
            throw Exception("Could not write attachment", e)
        }
    }
}
