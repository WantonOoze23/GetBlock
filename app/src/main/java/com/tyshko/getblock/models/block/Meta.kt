package com.tyshko.getblock.models.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Meta(
    val fee: Int,
    val innerInstructions: List<JsonElement>? = null,
    val logMessages: List<String>? = null,
)