package com.tyshko.getblock.models.block

import kotlinx.serialization.Serializable

@Serializable
data class BlockResult(
    val blockHeight: Int,
    val blockTime: Long,
    val blockhash: String,
    val parentSlot: Int,
    val previousBlockhash: String,
    val rewards: List<Reward>,
    val transactions: List<Transaction>
)