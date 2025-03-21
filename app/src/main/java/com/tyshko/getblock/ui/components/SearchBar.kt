package com.tyshko.getblock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tyshko.getblock.ui.screens.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(text: String, onSearchClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                text = "Search transactions, blocks, programs and tokens",
                onSearchClick = {

                },

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
