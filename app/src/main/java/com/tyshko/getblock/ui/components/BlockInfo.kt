package com.tyshko.getblock.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.tyshko.getblock.models.stack.Block
import com.tyshko.getblock.ui.screens.AppColors

@Composable
fun BlockInfo(
    blockList: List<Block>,
    onBlockClick: (Block) -> Unit
) {
    Card(
        modifier = Modifier
            .background(AppColors.Background, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            BlockHeader()
            Column {
                blockList.reversed().forEach { block ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBlockClick(block) },
                        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                    ) {
                        BlockRow(
                            signature = block.signature,
                            time = block.time,
                            block = block.block.toString()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BlockHeader(
    modifier: Modifier = Modifier
) {
    Column(
        Modifier.padding(vertical = 5.dp)
    ) {
        Row {
            Text(
                modifier = modifier
                    .weight(4.0f),
                text = "Signature",
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = modifier
                    .weight(3.0f),
                text = "Time",
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = modifier
                    .weight(3.0f),
                text = "Block",
                fontWeight = FontWeight.Bold,
            )
        }
        HorizontalDivider(
            modifier = modifier
                .fillMaxWidth()
        )
    }
}

@Composable
fun BlockRow(
    modifier: Modifier = Modifier,
    signature: String = "Loading...",
    time: Long = 1,
    block: String = "Loading..."
){
    Row(
        modifier = modifier
            .padding(bottom = 8.dp)
    ) {
        Text(
            modifier = modifier
                .weight(4.0f),
            text = signature,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = AppColors.Blue,
        )
        Text(
            modifier = modifier
                .weight(3.0f),
            text = calculateTime(time),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )
        Text(
            modifier = modifier
                .weight(3f),
            text = block,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start,
            color = AppColors.Blue,
        )
    }
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