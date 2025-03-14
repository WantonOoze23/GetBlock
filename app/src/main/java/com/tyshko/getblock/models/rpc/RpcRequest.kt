package com.tyshko.getblock.models.rpc

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class RpcRequest(
    val id: String,
    val jsonrpc: String,
    val params: List<JsonElement>? = emptyList(),
    val method: String
)