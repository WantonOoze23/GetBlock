package com.tyshko.getblock.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.tyshko.getblock.FakeData
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BlockInfoTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun blockInfo_displays_list_and_header() {
        val blocks = FakeData.sampleStack.blocks

        composeTestRule.setContent {
            BlockInfo(
                blockList = blocks,
                onBlockClick = {}
            )
        }

        // Перевіряємо заголовок таблиці
        composeTestRule.onNodeWithText("Signature").assertIsDisplayed()
        composeTestRule.onNodeWithText("Time").assertIsDisplayed()

        // Перевіряємо, що блоки відображаються
        // У BlockInfo використовується list.reversed(), тому перевіряємо порядок або наявність
        blocks.forEach { block ->
            composeTestRule.onNodeWithText(block.signature).assertIsDisplayed()
            composeTestRule.onNodeWithText(block.block.toString()).assertIsDisplayed()
        }
    }

    @Test
    fun blockInfo_click_triggers_callback() {
        val blocks = listOf(FakeData.sampleBlock)
        var clickedBlockId: Long? = null

        composeTestRule.setContent {
            BlockInfo(
                blockList = blocks,
                onBlockClick = { clickedBlockId = it.block }
            )
        }

        // Клікаємо по сигнатурі блоку
        composeTestRule.onNodeWithText(blocks[0].signature).performClick()

        assertEquals(blocks[0].block, clickedBlockId)
    }
}