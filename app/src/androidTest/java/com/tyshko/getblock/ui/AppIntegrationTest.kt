package com.tyshko.getblock.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tyshko.getblock.FakeData
import com.tyshko.getblock.data.repository.RpcRepository
import com.tyshko.getblock.models.block.BlockResult
import com.tyshko.getblock.models.epoch.EpochResult
import com.tyshko.getblock.models.rpc.RpcResponse
import com.tyshko.getblock.models.supply.Context
import com.tyshko.getblock.models.supply.GetSupply
import com.tyshko.getblock.models.supply.Value
import com.tyshko.getblock.ui.screens.BlockPage
import com.tyshko.getblock.ui.screens.MainPage
import com.tyshko.getblock.view.GetBlockViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest

class AppIntegrationTest : KoinTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var mockRepository: RpcRepository

    @Before
    fun setUp() {
        stopKoin()

        mockRepository = mockk(relaxed = true)

        setupMockResponses()

        startKoin {
            modules(
                module {
                    single { mockRepository }
                    viewModel { GetBlockViewModel(get()) }
                }
            )
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun fullAppFlow_loadsData_displaysList_and_navigatesToDetails() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            // Отримуємо VM через Koin, як у реальному додатку
            val viewModel: GetBlockViewModel = koinViewModel()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    MainPage(
                        viewModel = viewModel,
                        navController = navController
                    )
                }
                composable("block") {
                    BlockPage(viewModel = viewModel)
                }
            }
        }

        composeTestRule.onNodeWithText("GetBlock").assertIsDisplayed()
        composeTestRule.onNodeWithText("Explore Solana Blockchain").assertIsDisplayed()

        composeTestRule.waitUntil(timeoutMillis = 5000) {
            try {
                composeTestRule.onNodeWithText("100000000").assertIsDisplayed()
                true
            } catch (e: AssertionError) { false }
        }

        composeTestRule.onNodeWithText("205").assertIsDisplayed()

        val firstBlockSig = FakeData.sampleBlock.signature
        composeTestRule.onNodeWithText(firstBlockSig).performScrollTo().assertIsDisplayed()


        // --- ЕТАП 2: Взаємодія та Навігація ---

        // Клікаємо на перший блок у списку
        composeTestRule.onNodeWithText(firstBlockSig).performClick()

        // --- ЕТАП 3: Перевірка Екрану Деталей ---

        // Перевіряємо, що ми перейшли на екран деталей (з'явився заголовок "Block details")
        composeTestRule.waitUntil(timeoutMillis = 3000) {
            try {
                composeTestRule.onNodeWithText("Block details").assertIsDisplayed()
                true
            } catch (e: AssertionError) { false }
        }

        // Перевіряємо деталі конкретного блоку (Signature має співпадати)
        composeTestRule.onNodeWithText(firstBlockSig).assertIsDisplayed()

        // Перевіряємо попередній хеш блоку
        composeTestRule.onNodeWithText(FakeData.sampleBlock.previousBlockHash).assertIsDisplayed()
    }

    private fun setupMockResponses() {
        // --- Supply ---
        val supplyValue = Value(
            circulating = 50_000_000L,
            nonCirculating = 50_000_000L,
            nonCirculatingAccounts = emptyList(),
            total = 100_000_000L
        )
        val supplyResponse = RpcResponse("id", "2.0", GetSupply(Context("1", 1), supplyValue), null)
        coEvery { mockRepository.getSupply() } returns supplyResponse

        // --- Epoch ---
        val epochResult = EpochResult(
            absoluteSlot = 100, blockHeight = 100, epoch = 205,
            slotIndex = 1, slotsInEpoch = 432000, transactionCount = 100
        )
        val epochResponse = RpcResponse("id", "2.0", epochResult, null)
        coEvery { mockRepository.getEpoch() } returns epochResponse

        // --- Blocks List ---
        val blockIds = FakeData.blocksList.map { it.block }
        coEvery { mockRepository.getBlocks(any(), any()) } returns blockIds

        // --- Individual Block Details ---
        coEvery { mockRepository.getBlock(any()) } answers {
            val id = firstArg<Long>()
            val fakeBlock = FakeData.blocksList.find { it.block == id } ?: FakeData.sampleBlock

            val blockResult = mockk<BlockResult>(relaxed = true) {
                every { blockhash } returns fakeBlock.signature
                every { blockTime } returns fakeBlock.time
                every { previousBlockhash } returns fakeBlock.previousBlockHash
                every { rewards } returns listOf(mockk(relaxed = true) {
                    every { lamports } returns fakeBlock.rewardLamports
                })
            }
            RpcResponse("id", "2.0", blockResult, null)
        }
    }
}