package com.tyshko.getblock.data

import android.util.Log
import com.tyshko.getblock.data.repository.RpcRepositoryImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.toByteReadPacket
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.readText
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RpcRepositoryImplTest {

    private lateinit var repository: RpcRepositoryImpl

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getEpoch returns parsed EpochResult`() = runTest {
        // ARRANGE
        val mockResponse = """
            {
                "jsonrpc": "2.0",
                "result": {
                    "epoch": 100,
                    "absoluteSlot": 5000,
                    "slotsInEpoch": 432000,
                    "blockHeight": 123456,
                    "slotIndex": 10,
                    "transactionCount": 500
                },
                "id": "1"
            }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            assertEquals(HTTPRouts.BASE_URL, request.url.toString())
            assertEquals(HTTPRouts.X_API_KEY, request.headers["x-api-key"])

            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(jsonConfig)
            }
        }

        repository = RpcRepositoryImpl(client)

        // ACT
        val result = repository.getEpoch()

        // ASSERT
        assertEquals(100, result.result.epoch)
        assertEquals(5000, result.result.absoluteSlot)
    }

    @Test
    fun `getSupply returns parsed GetSupply`() = runTest {
        // ARRANGE
        val mockResponse = """
            {
                "jsonrpc": "2.0",
                "result": {
                    "context": { 
                        "slot": 123,
                        "apiVersion": "1.18.0" 
                    },
                    "value": {
                        "total": 500000,
                        "circulating": 100000,
                        "nonCirculating": 400000,
                        "nonCirculatingAccounts": []
                    }
                },
                "id": "1"
            }
        """.trimIndent()

        val mockEngine = MockEngine {
            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(jsonConfig) }
        }
        repository = RpcRepositoryImpl(client)

        // ACT
        val result = repository.getSupply()

        // ASSERT
        assertEquals(500000L, result.result.value.total)
        assertEquals(100000L, result.result.value.circulating)
    }

    @Test
    fun `getBlock sends correct params and returns BlockResult`() = runTest {
        // ARRANGE
        val blockNumber = 12345L

        val mockResponse = """
            {
                "jsonrpc": "2.0",
                "result": {
                    "blockhash": "hash123",
                    "previousBlockhash": "prev123",
                    "parentSlot": 12344,
                    "blockTime": 1000000,
                    "blockHeight": 12345,
                    "transactions": [],
                    "rewards": []
                },
                "id": "1"
            }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            val bodyString = request.body.toByteReadPacket().readText()

            assert(bodyString.contains("getBlock"))
            assert(bodyString.contains(blockNumber.toString()))
            assert(bodyString.contains("maxSupportedTransactionVersion"))

            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(jsonConfig) }
        }
        repository = RpcRepositoryImpl(client)

        // ACT
        val result = repository.getBlock(blockNumber)

        // ASSERT
        assertEquals("hash123", result.result.blockhash)

        assertEquals(12345, result.result.blockHeight)
    }

    @Test
    fun `getBlocks parses list of longs correctly`() = runTest {
        // ARRANGE
        val startSlot = 100L
        val endSlot = 105L

        val mockResponse = """
            {
                "jsonrpc": "2.0",
                "result": [100, 101, 102, 103, 104, 105],
                "id": "1"
            }
        """.trimIndent()

        val mockEngine = MockEngine { request ->
            val bodyString = request.body.toByteReadPacket().readText()
            assert(bodyString.contains("getBlocks"))
            assert(bodyString.contains(startSlot.toString()))
            assert(bodyString.contains(endSlot.toString()))

            respond(
                content = mockResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(jsonConfig) }
        }
        repository = RpcRepositoryImpl(client)

        // ACT
        val result = repository.getBlocks(startSlot, endSlot)

        // ASSERT
        assertEquals(6, result.size)
        assertEquals(100L, result[0])
        assertEquals(105L, result[5])
    }
}