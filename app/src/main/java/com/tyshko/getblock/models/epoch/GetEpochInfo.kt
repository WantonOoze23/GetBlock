package com.tyshko.getblock.models.epoch

data class GetEpochInfo(
    val id: Int,
    val jsonrpc: String,
    val result: Result
)