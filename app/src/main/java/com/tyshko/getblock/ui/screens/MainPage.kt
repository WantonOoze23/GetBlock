package com.tyshko.getblock.ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyshko.getblock.ui.components.BlockCard
import com.tyshko.getblock.view.GetBlockViewModel

@Composable
fun MainPage(viewModel: GetBlockViewModel) {
    var text by remember { mutableStateOf("") }

    val stack by viewModel.stack.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchEpoch()
        viewModel.fetchSupply()
    }

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
                /*SearchBar(
                    text = searchText,
                    onTextChange = { searchText = it },
                    onSearchClick = { /* TODO: Реализовать поиск */ },
                    modifier = Modifier.fillMaxWidth()
                )*/
            }
            Icon(
                modifier = Modifier.statusBarsPadding(),
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Search",
                tint = Color.White
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppColors.BackgroundInfo)
                .padding(horizontal =  20.dp)
                .padding(top = 10.dp)
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
                Text(text = stack.toString())
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
