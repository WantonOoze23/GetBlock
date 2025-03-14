package com.tyshko.getblock.models.block

data class Message(
    val accountKeys: List<String>,
    val instructions: List<Instruction>
)