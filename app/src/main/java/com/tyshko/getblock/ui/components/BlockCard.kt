package com.tyshko.getblock.ui.components


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import com.tyshko.getblock.ui.screens.AppColors

@Composable
fun BlockCard(
    modifier: Modifier = Modifier,

    title: String  = "",
    mainValue: String = "",

    firstSubTitle: String = "",
    firstSubValue: String = "",

    secondSubTitle: String = "",
    secondSubValue: String = "",

    onClick: () -> Unit = {},
){
    Card(
        onClick = onClick,
        modifier = Modifier
            .background(AppColors.Background, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = mainValue,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .background(AppColors.BackgroundInfo, shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = firstSubTitle)
                Text(
                    text = firstSubValue
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                )

                Text(
                    text = secondSubTitle
                )
                Text(
                    text = secondSubValue
                )
            }
        }
    }
}