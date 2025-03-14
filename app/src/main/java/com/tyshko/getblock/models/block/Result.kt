package com.tyshko.getblock.models.block

data class Result(
    val blockHeight: Int,
    val blockTime: Int,
    val blockhash: String,
    val parentSlot: Int,
    val previousBlockhash: String,
    val rewards: List<Reward>,
    val transactions: List<Transaction>
)