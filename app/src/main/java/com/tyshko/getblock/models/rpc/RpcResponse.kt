package com.tyshko.getblock.models.rpc

import kotlinx.serialization.Serializable

@Serializable
data class RpcResponse<T>(
    val id: String,
    val jsonrpc: String,
    val result: T,
    val error: RpcError? = null
)

@Serializable
data class RpcError(
    val code: Int,
    val message: String
)