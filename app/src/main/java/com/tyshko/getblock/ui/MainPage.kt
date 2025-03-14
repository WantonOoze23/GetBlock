package com.tyshko.getblock.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tyshko.getblock.view.GetBlockViewModel

@Composable
fun MainPage(viewModel: GetBlockViewModel) {
    var text by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchEpoch()
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
                SearchBar(text, { text = it }, { /* TODO: обработка поиска */ })
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = AppColors.Background)
                .padding(20.dp)
        ) {
            SOLSupplyBlock()
            Spacer(Modifier.height(20.dp))
            CurrentEpochBlock(viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(text: String, onTextChange: (String) -> Unit, onSearchClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("Search transactions, blocks, programs and tokens") },
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .size(32.dp)
                    .background(AppColors.Pink, shape = RoundedCornerShape(10.dp))
                    .padding(1.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun SOLSupplyBlock() {
    Box(
        modifier = Modifier
            .background(color = AppColors.BackgroundInfo)
            .height(100.dp)
    ) {
        Column {
            Text(
                text = "SOL Supply",
                fontSize = 20.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "596,037,910.89",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun CurrentEpochBlock(viewModel: GetBlockViewModel) {
    val epoch by viewModel.epoch.collectAsState()

    Box(
        modifier = Modifier
            .background(color = AppColors.BackgroundInfo)
            .height(100.dp)
    ) {
        Column {
            Text(
                text = "Epoch",
                fontSize = 20.sp
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = epoch ?: "Loading...",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}


object AppColors {
    val Pink = Color(0xFFD150EF)
    val Blue = Color(0xFF0B6FDA)
    val Turquoise = Color(0xFF00E8B4)
    val Background = Color(0xFFf6f6f6)
    val BackgroundInfo = Color(0x0FFFFFFF)
}
