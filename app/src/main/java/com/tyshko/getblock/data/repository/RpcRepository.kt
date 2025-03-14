package com.tyshko.getblock.data.repository

import com.tyshko.getblock.data.HTTPRouts
import com.tyshko.getblock.models.epoch.GetEpochInfo
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

class RpcRepository {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = Logger.DEFAULT
        }
    }

    suspend fun getEpoch(): RpcResponse<GetEpochInfo>{
        val request = RpcRequest(jsonrpc = "2.0", id = "getblock.io", method = "getEpochInfo", params = emptyList())

        val response = client.post(HTTPRouts.BASE_URL){
            contentType(ContentType.Application.Json)
            header("x-api-key", HTTPRouts.X_API_KEY)
            setBody(request)
        }.body<RpcResponse<GetEpochInfo>>()

        return response
    }

    suspend fun getSupply(): RpcResponse<GetSupply>{
        val request = RpcRequest(jsonrpc = "2.0", id = "getblock.io", method = "getSupply")

        val response = client.post(HTTPRouts.BASE_URL){
            contentType(ContentType.Application.Json)
            header("x-api-key", HTTPRouts.X_API_KEY)
            setBody(request)
        }.body<RpcResponse<GetSupply>>()

        return response
    }



}