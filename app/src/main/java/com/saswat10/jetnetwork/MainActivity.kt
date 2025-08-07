package com.saswat10.jetnetwork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.saswat10.jetnetwork.presentation.auth.login.LoginScreen
import com.saswat10.jetnetwork.presentation.post.Post
import com.saswat10.jetnetwork.presentation.posts_list.PostListScreen
import com.saswat10.jetnetwork.ui.theme.JetNetworkTheme
import com.saswat10.jetnetwork.utils.DEFAULT_POST_ID
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
                    Post(noteId = DEFAULT_POST_ID, modifier = Modifier.padding(padding))
                }
            }
        }

    }

}
