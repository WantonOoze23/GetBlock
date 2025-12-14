package com.tyshko.getblock.view

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tyshko.getblock.data.repository.RpcRepository
import com.tyshko.getblock.models.block.BlockResult
import com.tyshko.getblock.models.epoch.EpochResult
import com.tyshko.getblock.models.rpc.RpcResponse
import com.tyshko.getblock.models.supply.Context
import com.tyshko.getblock.models.supply.GetSupply
import com.tyshko.getblock.models.supply.Value
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetBlockViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: RpcRepository
    private lateinit var viewModel: GetBlockViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()

        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `fetchEpoch updates stack state correctly when repository returns success`() = runTest {
        // GIVEN
        val epoch = 100
        val slotsInEpoch = 432000
        val absoluteSlot = 43200500

        val mockEpochResult = EpochResult(
            absoluteSlot = absoluteSlot,
            blockHeight = 200,
            epoch = epoch,
            slotIndex = 500,
            slotsInEpoch = slotsInEpoch,
            transactionCount = 1000L
        )

        val epochResponse = RpcResponse(
            id = "test",
            jsonrpc = "2.0",
            result = mockEpochResult,
            error = null
        )

        coEvery { repository.getEpoch() } returns epochResponse
        coEvery { repository.getSupply() } throws Exception("Supply skipped")
        coEvery { repository.getBlocks(any(), any()) } returns emptyList()

        // WHEN
        viewModel = GetBlockViewModel(repository)

        // Виконуємо задачі до першого delay()
        testDispatcher.scheduler.runCurrent()

        // THEN
        val currentState = viewModel.stack.value

        // 2. ВИПРАВЛЕННЯ ТИПІВ:
        // У ViewModel: epoch(Int) * slots(Int) = Int.
        // Тому очікуваний результат має бути Int, або треба приводити обидва до Long.

        val expectedSlotRangeStart = epoch * slotsInEpoch
        val expectedSlotRangeEnd = expectedSlotRangeStart + slotsInEpoch - 1

        assertEquals("Epoch should match", epoch, currentState.epoch)

        assertEquals("SlotRangeStart check", expectedSlotRangeStart, currentState.slotRangeStart)
        assertEquals("SlotRangeEnd check", expectedSlotRangeEnd, currentState.slotRangeEnd)

        assert(currentState.timeRemaining.isNotEmpty())

        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }
    @Test
    fun `fetchSupply calculates percentages and updates state correctly`() = runTest {
        // GIVEN
        val circulating = 50_000_000L
        val nonCirculating = 50_000_000L
        val total = 100_000_000L

        val value = Value(
            circulating = circulating,
            nonCirculating = nonCirculating,
            nonCirculatingAccounts = emptyList(),
            total = total
        )
        val response = RpcResponse("id", "2.0", GetSupply(Context("1", 1), value), null)

        coEvery { repository.getSupply() } returns response
        coEvery { repository.getEpoch() } throws Exception("Ignore Epoch")

        // WHEN
        viewModel = GetBlockViewModel(repository)
        testDispatcher.scheduler.runCurrent() // Запускаємо init блок

        // THEN
        val state = viewModel.stack.value

        assertEquals("Circulating Supply mismatch", circulating, state.circulatingSupply)
        assertEquals("Total Supply mismatch", total, state.totalSupply)

        assertEquals("Circulating Percent mismatch", 50.0, state.percentCirculatingSupply, 0.01)
        assertEquals("Non-Circulating Percent mismatch", 50.0, state.percentNonCirculatingSupply, 0.01)

        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    @Test
    fun `fetchBlock updates currentBlock state correctly`() = runTest {
        // GIVEN
        coEvery { repository.getEpoch() } throws Exception("Ignore")
        coEvery { repository.getSupply() } throws Exception("Ignore")
        viewModel = GetBlockViewModel(repository)

        val blockNum = 12345L
        val blockHash = "hash_123"
        val blockTime = 99999L

        val blockResult = mockk<BlockResult>(relaxed = true)
        every { blockResult.blockhash } returns blockHash
        every { blockResult.blockTime } returns blockTime
        every { blockResult.rewards } returns listOf(mockk(relaxed = true))

        val response = RpcResponse("id", "2.0", blockResult, null)
        coEvery { repository.getBlock(blockNum) } returns response

        // WHEN
        viewModel.fetchBlock(blockNum)
        testDispatcher.scheduler.runCurrent()

        // THEN
        val currentBlock = viewModel.stack.value.currentBlock

        assertEquals("Block number mismatch", blockNum, currentBlock.block)
        assertEquals("Block signature mismatch", blockHash, currentBlock.signature)
        assertEquals("Block time mismatch", blockTime, currentBlock.time)

        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    @Test
    fun `fetchBlocks updates list of blocks correctly`() = runTest {
        // GIVEN
        coEvery { repository.getEpoch() } throws Exception("Ignore")
        coEvery { repository.getSupply() } throws Exception("Ignore")
        viewModel = GetBlockViewModel(repository)

        val startSlot = 100L
        val epoch = 5
        val listOfSlots = listOf(101L, 102L) // Повернулись 2 слоти

        coEvery { repository.getBlocks(startSlot, any()) } returns listOfSlots

        val blockResult1 = mockk<BlockResult>(relaxed = true) {
            every { blockTime } returns 1000L
            every { blockhash } returns "hash_101"
            every { rewards } returns listOf(mockk(relaxed = true))
        }
        val blockResult2 = mockk<BlockResult>(relaxed = true) {
            every { blockTime } returns 2000L
            every { blockhash } returns "hash_102"
            every { rewards } returns listOf(mockk(relaxed = true))
        }

        coEvery { repository.getBlock(101L) } returns RpcResponse("id", "2.0", blockResult1, null)
        coEvery { repository.getBlock(102L) } returns RpcResponse("id", "2.0", blockResult2, null)

        // WHEN
        viewModel.fetchBlocks(startSlot, epoch = epoch)
        testDispatcher.scheduler.runCurrent()

        // THEN
        val blocks = viewModel.stack.value.blocks
        assertEquals("Should have 2 blocks", 2, blocks.size)
        assertEquals("First block hash mismatch", "hash_101", blocks[0].signature)
        assertEquals("Second block hash mismatch", "hash_102", blocks[1].signature)

        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    @Test
    fun `setCurrentBlock updates state immediately`() = runTest {
        // Цей метод не запускає корутин, тому тут простіше
        coEvery { repository.getEpoch() } throws Exception("Ignore")
        coEvery { repository.getSupply() } throws Exception("Ignore")
        viewModel = GetBlockViewModel(repository)

        val newBlock = com.tyshko.getblock.models.stack.Block(
            time = 123, block = 1, signature = "manual", epoch = 1, rewardLamports = 0, previousBlockHash = ""
        )

        // WHEN
        viewModel.setCurrentBlock(newBlock)

        // THEN
        assertEquals(newBlock, viewModel.stack.value.currentBlock)

        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    @Test
    fun `fetchBlocks takes only last 5 blocks defined by amountOfBlock constant`() = runTest {
        // GIVEN
        coEvery { repository.getEpoch() } throws Exception("Ignore")
        coEvery { repository.getSupply() } throws Exception("Ignore")
        viewModel = GetBlockViewModel(repository)

        val startSlot = 100L
        // Генеруємо список із 7 блоків (більше ніж ліміт 5)
        val lotsOfSlots = (1L..7L).toList()

        // Мокаємо повернення 7 слотів
        coEvery { repository.getBlocks(startSlot, any()) } returns lotsOfSlots

        // Мокаємо деталі для будь-якого блоку
        // Ми використовуємо slot як частину хешу, щоб перевірити, які саме блоки потрапили
        coEvery { repository.getBlock(any()) } answers {
            val slot = firstArg<Long>()
            val result = mockk<BlockResult>(relaxed = true) {
                every { blockhash } returns "hash_$slot"
                every { blockTime } returns 1000L + slot
                every { rewards } returns listOf(mockk(relaxed = true))
            }
            RpcResponse("id", "2.0", result, null)
        }

        // WHEN
        viewModel.fetchBlocks(startSlot, epoch = 1)
        testDispatcher.scheduler.runCurrent()

        // THEN
        val blocks = viewModel.stack.value.blocks

        // 1. Перевіряємо, що розмір списку обмежено 5
        assertEquals("Should only take last 5 blocks", 5, blocks.size)

        // 2. Перевіряємо, що взято саме ОСТАННІ елементи (3, 4, 5, 6, 7)
        // Перший елемент у списку має бути hash_3 (бо 1 і 2 відкинуті)
        assertEquals("First block should be the 3rd one", "hash_3", blocks[0].signature)
        // Останній елемент має бути hash_7
        assertEquals("Last block should be the 7th one", "hash_7", blocks[4].signature)

        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }

    @Test
    fun `fetchSupply handles zero values without arithmetic exceptions`() = runTest {
        // GIVEN
        // Сценарій: Тільки запустили мережу, або тестова мережа, де нічого не циркулює
        val circulating = 0L
        val nonCirculating = 100L
        val total = 100L

        val value = Value(circulating, nonCirculating, emptyList(), total)
        val response = RpcResponse("id", "2.0", GetSupply(Context("1", 1), value), null)

        coEvery { repository.getSupply() } returns response
        coEvery { repository.getEpoch() } throws Exception("Ignore")

        // WHEN
        viewModel = GetBlockViewModel(repository)
        testDispatcher.scheduler.runCurrent()

        // THEN
        val state = viewModel.stack.value

        assertEquals("Circulating percent should be 0.0", 0.0, state.percentCirculatingSupply, 0.01)
        // (100 / 100) * 100 = 100.0
        assertEquals("Non-Circulating percent should be 100.0", 100.0, state.percentNonCirculatingSupply, 0.01)

        viewModel.viewModelScope.coroutineContext.cancelChildren()
    }
}