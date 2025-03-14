package com.tyshko.getblock.models.block

data class GetBlock(
    val id: String,
    val jsonrpc: String,
    val result: Result
)