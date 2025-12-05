package com.tyshko.getblock.models.block

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@Serializable
@JsonIgnoreUnknownKeys
data class BlockResult(
    val blockHeight: Int,
    val blockTime: Long,
    val blockhash: String,
    val parentSlot: Int,
    val previousBlockhash: String,
    val rewards: List<Reward>,
    val transactions: List<Transaction>
)