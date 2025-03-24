package com.tyshko.getblock.models.block

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val accountKeys: List<String>,
    val instructions: List<Instruction>
)