package com.saswat10.jetnetwork

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.saswat10.jetnetwork.presentation.accounts_center.AccountsScreen
import com.saswat10.jetnetwork.presentation.auth.login.LoginScreen
import com.saswat10.jetnetwork.presentation.auth.register.RegisterScreen
import com.saswat10.jetnetwork.presentation.feed.FeedScreen
import com.saswat10.jetnetwork.presentation.post.Post
import com.saswat10.jetnetwork.ui.theme.JetNetworkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable


@Composable
fun JNApp() {
    JetNetworkTheme {
        Surface(color = MaterialTheme.colorScheme.background){
            val snackbarHostState = remember { SnackbarHostState() }
            val appState = rememberAppState(snackbarHostState)

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState)}
            ) { paddingValues ->
                NavHost(
                    modifier = Modifier.padding(paddingValues),
                    startDestination = FeedScreen,
                    navController = appState.navController as NavHostController,
                ) {
                    jetnetworkGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): JNAppState {
    return remember(snackbarHostState, navController, snackbarManager, coroutineScope) {
        JNAppState(snackbarHostState, navController, snackbarManager, coroutineScope)
    }
}

fun NavGraphBuilder.jetnetworkGraph(appState: JNAppState){
    composable<FeedScreen>{
        FeedScreen(
            openScreen = { appState.navigate(it) }
        )
    }

    composable<PostScreen> {
        val args = it.toRoute<PostScreen>()
        Post(
            postId = args.postId,
            modifier = Modifier,
            popUpScreen = {appState.popUp()}
        )
    }

    composable<AccountsScreen> {
        AccountsScreen(modifier = Modifier)
    }

    composable<LoginScreen> {
        LoginScreen(modifier = Modifier)
    }

    composable<RegisterScreen> {
        RegisterScreen(modifier = Modifier)
    }
}



@Serializable
object FeedScreen

@Serializable
object AccountsScreen

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
data class PostScreen(val postId: String)