package com.tyshko.getblock.models.stack

data class Block (
    val block: Long,
    val signature: String,
    val time: Long,
    val epoch: Int,
    val rewardLamports: Long,
    val previousBlockHash: String
)