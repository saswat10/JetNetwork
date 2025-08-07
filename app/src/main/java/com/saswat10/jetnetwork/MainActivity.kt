package com.saswat10.jetnetwork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.saswat10.jetnetwork.presentation.feed.FeedScreen
import com.saswat10.jetnetwork.ui.theme.JetNetworkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalLayoutApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel by viewModels()
            LaunchedEffect(true) {
                mainViewModel.onAppStart()
            }
            JetNetworkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
//                    AccountsScreen(modifier = Modifier.padding(padding))
//                    LoginScreen(modifier = Modifier.padding(padding))
                    FeedScreen()
                }

            }
        }

    }

}
