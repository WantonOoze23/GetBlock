package com.tyshko.getblock.ui.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavHostController
import com.tyshko.getblock.FakeData
import com.tyshko.getblock.view.GetBlockViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class MainPageTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun mainPage_shows_supply_epoch_and_blocks() {
        // GIVEN
        // 1. Мокаємо ViewModel
        val viewModel = mockk<GetBlockViewModel>(relaxed = true)
        val stateFlow = MutableStateFlow(FakeData.sampleStack)

        // Налаштовуємо поведінку: коли звертаються до stack, повертаємо наш FakeData
        every { viewModel.stack } returns stateFlow

        // 2. Мокаємо NavController
        val navController = mockk<NavHostController>(relaxed = true)

        // WHEN
        composeTestRule.setContent {
            MainPage(
                viewModel = viewModel,
                navController = navController
            )
        }

        // THEN
        // 1. Перевірка заголовків
        composeTestRule.onNodeWithText("GetBlock").assertIsDisplayed()
        composeTestRule.onNodeWithText("Explore Solana Blockchain").assertIsDisplayed()

        // 2. Перевірка картки Supply
        composeTestRule.onNodeWithText("SOL Supply").assertIsDisplayed()
        composeTestRule.onNodeWithText(FakeData.sampleStack.totalSupply.toString()).assertIsDisplayed()

        // 3. Перевірка картки Epoch
        composeTestRule.onNodeWithText("Epoch").assertIsDisplayed()
        composeTestRule.onNodeWithText(FakeData.sampleStack.epoch.toString()).assertIsDisplayed()

        // 4. Перевірка наявності списку блоків
        // Шукаємо перший блок зі списку
        val firstBlockSig = FakeData.sampleStack.blocks.first().signature
        composeTestRule.onNodeWithText(firstBlockSig).performScrollTo().assertIsDisplayed()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Test
    fun mainPage_search_logic_shows_error_dialog_on_invalid_input() {
        // GIVEN
        val viewModel = mockk<GetBlockViewModel>(relaxed = true)
        val stateFlow = MutableStateFlow(FakeData.sampleStack) // Діапазон 123400..123500
        every { viewModel.stack } returns stateFlow

        val navController = mockk<NavHostController>(relaxed = true)

        composeTestRule.setContent {
            MainPage(viewModel = viewModel, navController = navController)
        }

        // WHEN
        // Вводимо число поза діапазоном (наприклад, 1)
        val searchInput = "1"
        composeTestRule.onNodeWithContentDescription("Search") // Іконка пошуку в LeadingIcon
            .performClick() // Це фокусує поле, але текст треба вводити в саме поле

        // Знаходимо поле вводу (SearchBar сам по собі складний, шукаємо по placeholder)
        composeTestRule.onNodeWithText("Search transactions, blocks, programs and tokens")
            .performTextInput(searchInput)

        // Натискаємо Enter (IME action) або клікаємо пошук.
        // У вашому коді `onSearch` викликається клавіатурою.
        composeTestRule.onNodeWithText(searchInput).performImeAction()

        // THEN
        // Має з'явитися діалог помилки
        composeTestRule.onNodeWithText("Error input").assertIsDisplayed()
        composeTestRule.onNodeWithText("Wrong input. Try one more time").assertIsDisplayed()
    }
}