package com.tyshko.getblock.models.epoch

import kotlinx.serialization.Serializable

@Serializable
data class GetEpochInfo(
    val result: EpochResult
)