package com.tyshko.getblock.models.epoch

import kotlinx.serialization.Serializable

@Serializable
data class EpochResult(
    val absoluteSlot: Int,
    val blockHeight: Int,
    val epoch: Int,
    val slotIndex: Int,
    val slotsInEpoch: Int,
    val transactionCount: Long
)