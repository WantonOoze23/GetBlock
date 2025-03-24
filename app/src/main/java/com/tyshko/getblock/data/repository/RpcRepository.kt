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

    suspend fun getEpoch(): RpcResponse<EpochResult>{
        val request = RpcRequest(method = "getEpochInfo")

        val response = client.post(HTTPRouts.BASE_URL){
            contentType(ContentType.Application.Json)
            header("x-api-key", HTTPRouts.X_API_KEY)
            setBody(request)
        }
        return response.body<RpcResponse<EpochResult>>()
    }

    suspend fun getSupply(): RpcResponse<GetSupply>{
        val request = RpcRequest(method = "getSupply")

        val response = client.post(HTTPRouts.BASE_URL){
            contentType(ContentType.Application.Json)
            header("x-api-key", HTTPRouts.X_API_KEY)
            setBody(request)
        }
        return response.body<RpcResponse<GetSupply>>()
    }

    suspend fun getBlock(blockNumber: Long): RpcResponse<BlockResult>{
        val request = RpcRequest(
            method = "getBlock",
            params = listOf(Json.encodeToJsonElement(blockNumber), Json.encodeToJsonElement(mapOf("maxSupportedTransactionVersion" to 0)))
        )

        val response = client.post(HTTPRouts.BASE_URL){
            contentType(ContentType.Application.Json)
            header("x-api-key", HTTPRouts.X_API_KEY)
            setBody(request)
        }
        Log.d("RPC", "get block response $response")
        return response.body<RpcResponse<BlockResult>>()
    }

    suspend fun getBlocks(startSlot: Long, endSlot: Long? = null): List<Long> {
        val params = if (endSlot != null) {
            listOf(Json.encodeToJsonElement(startSlot), Json.encodeToJsonElement(endSlot))
        } else {
            listOf(Json.encodeToJsonElement(startSlot), null)
        }

        val request = RpcRequest(
            method = "getBlocks",
            params = params.map { Json.encodeToJsonElement(it) }
        )

        val response = client.post(HTTPRouts.BASE_URL) {
            contentType(ContentType.Application.Json)
            header("x-api-key", HTTPRouts.X_API_KEY)
            setBody(request)
        }
        return response.body<RpcResponse<List<Long>>>().result
    }

    suspend fun getSlot(): RpcResponse<Long> {
        val request = RpcRequest(
            method = "getSlot"
        )

        val response = client.post(HTTPRouts.BASE_URL) {
            contentType(ContentType.Application.Json)
            header("x-api-key", HTTPRouts.X_API_KEY)
            setBody(request)
        }
        Log.d("RPC", "get slot response $response")
        return response.body<RpcResponse<Long>>()
    }
}