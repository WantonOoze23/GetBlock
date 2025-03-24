package com.tyshko.getblock.models.block

import kotlinx.serialization.Serializable

@Serializable
data class Reward(
    val lamports: Int,
    val postBalance: Long,
    val pubkey: String,
    val rewardType: String
)