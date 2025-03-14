package com.tyshko.getblock.models.block

data class Reward(
    val commission: Any,
    val lamports: Int,
    val postBalance: Long,
    val pubkey: String,
    val rewardType: String
)