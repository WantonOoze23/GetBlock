package com.tyshko.getblock.models.block

data class Meta(
    val err: Any,
    val fee: Int,
    val innerInstructions: List<Any>,
    val logMessages: List<String>,
    val status: Status
)