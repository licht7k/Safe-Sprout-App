package com.example.parentalcontrol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.parentalcontrol.ui.theme.ParentalControlTheme
import com.example.parentalcontrol.ui.navigation.AppNavGraph
import androidx.core.view.WindowCompat
import androidx.activity.enableEdgeToEdge
import android.graphics.Color
import androidx.lifecycle.lifecycleScope
import com.example.parentalcontrol.data.local.TokenStore
import com.example.parentalcontrol.data.remote.network.TokenProvider
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        val tokenStore = TokenStore(applicationContext)
        lifecycleScope.launch {
            tokenStore.tokenFlow.collect { token ->
                TokenProvider.token = token
            }
        }


        setContent {
            ParentalControlTheme {
                AppNavGraph()

            }
        }
    }
}

