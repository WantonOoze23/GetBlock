@file:OptIn(ExperimentalMaterial3Api::class)

package com.tyshko.getblock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.tyshko.getblock.ui.theme.GetBlockTheme
import com.tyshko.getblock.view.GetBlockViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            GetBlockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    val getBlockModel: GetBlockViewModel = koinViewModel()

                    AppNavigation(navController, getBlockModel)
                }
            }
        }
    }
}


