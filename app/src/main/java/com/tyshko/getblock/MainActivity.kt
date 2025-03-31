@file:OptIn(ExperimentalMaterial3Api::class)

package com.tyshko.getblock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.tyshko.getblock.ui.screens.MainPage
import com.tyshko.getblock.ui.screens.BlockPage
import com.tyshko.getblock.ui.theme.GetBlockTheme
import com.tyshko.getblock.view.GetBlockViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val getBlockModel = ViewModelProvider(this)[GetBlockViewModel::class.java]

        setContent {
            GetBlockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController, getBlockModel)
                }
            }
        }
    }
}

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
