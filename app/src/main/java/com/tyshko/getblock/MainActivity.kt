package com.tyshko.getblock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.tyshko.getblock.ui.screens.MainPage
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
                    MainPage(getBlockModel)
                }
            }
        }
    }
}
