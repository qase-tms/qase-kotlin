package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable

@Serializable
data class Relations (
    var suite: Suite = Suite(),
)
