package com.tyshko.getblock.models.block

data class Instruction(
    val accounts: List<Int>,
    val `data`: String,
    val programIdIndex: Int
)