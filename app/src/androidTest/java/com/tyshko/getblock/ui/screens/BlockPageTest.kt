package com.tyshko.getblock.ui.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.tyshko.getblock.FakeData
import com.tyshko.getblock.view.GetBlockViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class BlockPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun blockPage_displays_block_details_correctly() {
        // GIVEN
        val viewModel = mockk<GetBlockViewModel>(relaxed = true)
        val stateFlow = MutableStateFlow(FakeData.sampleStack)
        every { viewModel.stack } returns stateFlow

        // WHEN
        composeTestRule.setContent {
            BlockPage(viewModel = viewModel)
        }

        // THEN
        // Заголовок
        composeTestRule.onNodeWithText("Block details").assertIsDisplayed()

        // Деталі блоку
        val block = FakeData.sampleBlock

        // Перевіряємо підписи (Titles)
        composeTestRule.onNodeWithText("Signature").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reward").assertIsDisplayed()
        composeTestRule.onNodeWithText("Previous block").assertIsDisplayed()

        // Перевіряємо значення (Values)
        composeTestRule.onNodeWithText(block.block.toString()).assertIsDisplayed()
        composeTestRule.onNodeWithText(block.signature).assertIsDisplayed()
        composeTestRule.onNodeWithText(block.previousBlockHash).assertIsDisplayed()

        // Перевірка форматування нагороди (це складніше, бо там обчислення,
        // але ми знаємо вхідні дані: 5000000 lamports / 10^9 = 0.005 SOL)
        // 0.005 * 144.44 (ціна захардкоджена в BlockPage) = 0.72 USD
        // Очікуємо: "0.005000 SOL (0.72 USD)"
        val expectedRewardText = "0.005000 SOL (0.72 USD)"
        composeTestRule.onNodeWithText(expectedRewardText).assertIsDisplayed()
    }
}