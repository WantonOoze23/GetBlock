package com.tyshko.getblock.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class BlockCardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun blockCard_displays_correct_information() {
        // Given
        val title = "SOL Supply"
        val mainValue = "400,000,000"
        val subTitle1 = "Circulating"
        val subValue1 = "95%"
        val subTitle2 = "Non-Circulating"
        val subValue2 = "5%"

        // When
        composeTestRule.setContent {
            BlockCard(
                title = title,
                mainValue = mainValue,
                firstSubTitle = subTitle1,
                firstSubValue = subValue1,
                secondSubTitle = subTitle2,
                secondSubValue = subValue2
            )
        }

        // Then
        composeTestRule.onNodeWithText(title).assertIsDisplayed()
        composeTestRule.onNodeWithText(mainValue).assertIsDisplayed()
        composeTestRule.onNodeWithText(subTitle1).assertIsDisplayed()
        composeTestRule.onNodeWithText(subValue1).assertIsDisplayed()
        composeTestRule.onNodeWithText(subTitle2).assertIsDisplayed()
        composeTestRule.onNodeWithText(subValue2).assertIsDisplayed()
    }
}