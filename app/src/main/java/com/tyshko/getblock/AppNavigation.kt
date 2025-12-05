package com.tyshko.getblock

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tyshko.getblock.ui.screens.BlockPage
import com.tyshko.getblock.ui.screens.MainPage
import com.tyshko.getblock.view.GetBlockViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(navController: NavHostController, viewModel: GetBlockViewModel) {
    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainPage(
                viewModel,
                navController,
                onSearchClick = {
                    navController.navigate("block")
                }
            )
        }
        composable("block") {
            BlockPage(viewModel)
        }
    }
}