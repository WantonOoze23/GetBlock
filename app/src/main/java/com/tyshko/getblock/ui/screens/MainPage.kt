package com.tyshko.getblock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.tyshko.getblock.ui.components.BlockCard
import com.tyshko.getblock.ui.components.BlockInfo
import com.tyshko.getblock.ui.components.HeaderSearchBar
import com.tyshko.getblock.view.GetBlockViewModel

@ExperimentalMaterial3Api
@Composable
fun MainPage(
    viewModel: GetBlockViewModel,
    navController: NavHostController,
    onSearchClick: () -> Unit = {},
) {
    val stack by viewModel.stack.collectAsState()

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(AppColors.Pink, AppColors.Blue, AppColors.Turquoise)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "GetBlock",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(30.dp))
                Text(
                    text = "Explore Solana Blockchain",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(15.dp))
                HeaderSearchBar(
                    viewModel = viewModel,
                    onSearchClick = onSearchClick
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppColors.BackgroundInfo)
                .padding(horizontal = 20.dp)
                .padding(top = 10.dp, bottom = 10.dp)
        ) {
            item {
                BlockCard(
                    title = "SOL Supply",
                    mainValue = stack.totalSupply.toString(),
                    firstSubTitle = "Circulating Supply",
                    firstSubValue = "${stack.circulatingSupply} SOL (${String.format("%.1f", stack.percentCirculatingSupply)}%)",
                    secondSubTitle = "Non-circulating Supply",
                    secondSubValue = "${stack.nonCirculatingSupply} SOL (${String.format("%.1f", stack.percentNonCirculatingSupply)}%)",
                )
            }
            item {
                Spacer(Modifier.height(20.dp))
            }
            item {
                BlockCard(
                    title = "Epoch",
                    mainValue = stack.epoch.toString(),
                    firstSubTitle = "Slot Range",
                    firstSubValue = "${stack.slotRangeStart} to ${stack.slotRangeEnd}",
                    secondSubTitle = "Time Remain",
                    secondSubValue = stack.timeRemaining
                )
            }
            item {
                Spacer(Modifier.height(20.dp))
            }
            item {
                BlockInfo(
                    blockList = stack.blocks,
                    onBlockClick = { block ->
                        viewModel.setCurrentBlock(block)
                        navController.navigate("block")
                    }
                )
            }
        }
    }
}


object AppColors {
    val Pink = Color(0xFFD150EF)
    val Blue = Color(0xFF0B6FDA)
    val Turquoise = Color(0xFF00E8B4)
    val Background = Color(0xFFFFFFFF)
    val BackgroundInfo = Color(0xFFF1F1F1)
}
