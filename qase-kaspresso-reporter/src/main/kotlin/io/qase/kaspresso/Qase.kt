package io.qase.kaspresso

import io.qase.commons.kotlin.listener.Qase

object Qase {
    @JvmStatic
    fun id(id: Long) {
        Qase.listener.addIdToCurrentCase(id)
    }

    @JvmStatic
    fun title(title: String) {
        Qase.listener.addTitleToCurrentCase(title)
    }

    @JvmStatic
    fun fields(fields: Map<String, String>) {
        Qase.listener.addFieldsToCurrentCase(fields)
    }

    @JvmStatic
    fun comment(comment: String) {
        Qase.listener.addCommentToCurrentCase(comment)
    }

    @JvmStatic
    fun parameters(params: Map<String, String>) {
        Qase.listener.addParametersToCurrentCase(params)
    }
}
