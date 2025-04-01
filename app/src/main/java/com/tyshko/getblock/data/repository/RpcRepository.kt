package com.tyshko.getblock.data.repository

import android.util.Log
import com.tyshko.getblock.data.HTTPRouts
import com.tyshko.getblock.models.block.BlockResult
import com.tyshko.getblock.models.epoch.EpochResult
import com.tyshko.getblock.models.rpc.RpcRequest
import com.tyshko.getblock.models.rpc.RpcResponse
import com.tyshko.getblock.models.supply.GetSupply
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.encodeToJsonElement

class RpcRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }

    private suspend inline fun <reified T> executeRpcRequest(
        method: String,
        params: List<JsonElement>? = emptyList(),
        logTag: String? = null
    ): RpcResponse<T> {
        val request = params?.let { RpcRequest(method = method, params = it) }

        val response = client.post(HTTPRouts.BASE_URL) {
            contentType(ContentType.Application.Json)
            header("x-api-key", HTTPRouts.X_API_KEY)
            setBody(request)
        }

        logTag?.let {
            Log.d(it, "Response for $method: $response")
        }

        return response.body()
    }

    suspend fun getEpoch(): RpcResponse<EpochResult> {
        return executeRpcRequest(method = "getEpochInfo")
    }

    suspend fun getSupply(): RpcResponse<GetSupply> {
        return executeRpcRequest(method = "getSupply")
    }

    suspend fun getBlock(blockNumber: Long): RpcResponse<BlockResult> {
        val params = listOf(
            Json.encodeToJsonElement(blockNumber),
            Json.encodeToJsonElement(mapOf("maxSupportedTransactionVersion" to 0))
        )

        return executeRpcRequest(
            method = "getBlock",
            params = params,
            logTag = "RPC"
        )
    }

    suspend fun getBlocks(startSlot: Long, endSlot: Long? = null): List<Long> {
        val jsonParams = if (endSlot != null) {
            listOf(
                Json.encodeToJsonElement(startSlot),
                Json.encodeToJsonElement(endSlot)
            )
        } else {
            listOf(
                Json.encodeToJsonElement(startSlot),
                JsonNull
            )
        }

        return executeRpcRequest<List<Long>>(
            method = "getBlocks",
            params = jsonParams
        ).result
    }
}