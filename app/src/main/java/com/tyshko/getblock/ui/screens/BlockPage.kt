package com.tyshko.getblock.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import com.tyshko.getblock.models.stack.Block
import com.tyshko.getblock.models.stack.UiStack
import com.tyshko.getblock.view.GetBlockViewModel

@Composable
fun BlockPage(viewModel: GetBlockViewModel) {
    val block by viewModel.stack.collectAsState()

    val solToUSDT: Double = 144.44

    if (block == null) {
        Column(
            modifier = Modifier.statusBarsPadding().padding(horizontal = 10.dp)
        ) {
            Text("Loading...")
        }
        return
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(10.dp)
    ) {
        Text(
            text = "Block details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Column(
            Modifier
                .padding(vertical = 20.dp, horizontal = 20.dp)
                .fillMaxWidth()
        ) {
            BlockGrid(block, solToUSDT)
        }
    }
}

@Composable
fun BlockGrid(block: UiStack, solPrice: Double) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val items = listOf(
            "Block" to block.currentBlock.block.toString(),
            "Signature" to block.currentBlock.signature,
            "Time" to calculateTime(block.currentBlock.time),
            "Epoch" to block.currentBlock.epoch.toString(),
            "Reward" to calculatePrice(block.currentBlock.rewardLamports, solPrice),
            "Previous block" to block.currentBlock.previousBlockHash
        )

        items.forEachIndexed { index, (title, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (index == 0) 0.dp else 12.dp)
            ) {
                Text(
                    text = title,
                    fontSize = if (index == 0) 16.sp else 14.sp,
                    fontWeight = if (index == 0) FontWeight.Bold else FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )
                Text(
                    text = value,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.weight(1.5f)
                )
            }
        }
    }
}

fun calculatePrice(rewardLamports: Int, solToUSDT: Double): String {
    if (rewardLamports == null) return "N/A"

    val solReward = rewardLamports / 1_000_000_000.0
    val usdReward = solReward * solToUSDT

    return "%.6f SOL (%.2f USD)".format(solReward, usdReward)
}

fun calculateTime(timestamp: Long): String {
    val secondsAgo = (System.currentTimeMillis() / 1000) - timestamp
    val minutes = secondsAgo / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        days > 0 -> "$days d ago"
        hours > 0 -> "$hours h ago"
        minutes > 0 -> "$minutes m ago"
        else -> "$secondsAgo s ago"
    }
}
