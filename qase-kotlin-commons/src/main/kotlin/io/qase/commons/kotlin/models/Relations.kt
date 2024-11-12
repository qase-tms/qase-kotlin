package io.qase.commons.kotlin.models

import kotlinx.serialization.Serializable

@Serializable
data class Relations (
    val suite: Suite = Suite(),
)
