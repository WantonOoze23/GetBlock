package com.tyshko.getblock.models.block

import kotlinx.serialization.Serializable

@Serializable
data class Instruction(
    val accounts: List<Int>,
    val `data`: String,
    val programIdIndex: Int
)