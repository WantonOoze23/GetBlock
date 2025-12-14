package com.tyshko.getblock

import com.tyshko.getblock.models.stack.Block
import com.tyshko.getblock.models.stack.UiStack

object FakeData {
    val sampleBlock = Block(
        block = 123456L,
        signature = "5K7x...Sig",
        time = System.currentTimeMillis() / 1000 - 600, // 10 хвилин тому
        epoch = 205,
        rewardLamports = 5000000,
        previousBlockHash = "4L8y...Prev"
    )

    val sampleStack = UiStack(
        epoch = 205,
        slotRangeStart = 123400,
        slotRangeEnd = 123500,
        timeRemaining = "2d 4h",
        circulatingSupply = 400000000L,
        nonCirculatingSupply = 10000000L,
        totalSupply = 410000000L,
        percentCirculatingSupply = 95.5,
        percentNonCirculatingSupply = 4.5,
        currentBlock = sampleBlock,
        blocks = listOf(
            sampleBlock,
            sampleBlock.copy(block = 123455L, signature = "OldSig1"),
            sampleBlock.copy(block = 123454L, signature = "OldSig2")
        )
    )

    val blocksList = listOf(
        sampleBlock,
        sampleBlock.copy(block = 123455L, signature = "OldSig1"),
        sampleBlock.copy(block = 123454L, signature = "OldSig2")
    )
}