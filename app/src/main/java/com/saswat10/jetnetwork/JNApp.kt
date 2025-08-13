package com.saswat10.jetnetwork

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.saswat10.jetnetwork.presentation.accounts_center.AccountsScreen
import com.saswat10.jetnetwork.presentation.auth.login.LoginScreen
import com.saswat10.jetnetwork.presentation.auth.register.RegisterScreen
import com.saswat10.jetnetwork.presentation.chat.chat.ChatScreen
import com.saswat10.jetnetwork.presentation.chat.chat_list.ChatListScreen
import com.saswat10.jetnetwork.presentation.feed.FeedScreen
import com.saswat10.jetnetwork.presentation.post.Post
import com.saswat10.jetnetwork.ui.theme.JetNetworkTheme
import com.saswat10.jetnetwork.utils.NavDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JNApp() {
    JetNetworkTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val snackbarHostState = remember { SnackbarHostState() }
            val appState = rememberAppState(snackbarHostState)
            val scrollBehavior =
                TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())


            val items =
                listOf(NavDestination.Feed, NavDestination.Conversation, NavDestination.Accounts)
            var selectedIndex by remember { mutableIntStateOf(0) }

            val entry by appState.navController.currentBackStackEntryAsState()
            val currentDestination = entry?.destination

            val shouldShowBottomBar = currentDestination?.let { destination ->
                items.any { navDestination ->
                    destination.hasRoute(navDestination.route::class)
                }
            } ?: false

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    JNTopAppBar(
                        modifier = Modifier,
                        navController = appState.navController,
                        scrollBehavior = scrollBehavior
                    )
                },
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                bottomBar = {
                    AnimatedVisibility(shouldShowBottomBar) {
                        NavigationBar {
                            items.forEachIndexed { index, destination ->
                                NavigationBarItem(
                                    icon = { Icon(painterResource(destination.icon), null) },
                                    label = { Text(destination.title) },
                                    selected = index == selectedIndex,
                                    onClick = {
                                        selectedIndex = index
                                        appState.navController.navigate(destination.route) {
                                            popUpTo(appState.navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
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

fun NavGraphBuilder.jetnetworkGraph(appState: JNAppState) {
    composable<FeedScreen> {
        FeedScreen(
            openScreen = { appState.navigate(it) }
        )

    }

    composable<PostScreen> {
        val args = it.toRoute<PostScreen>()
        Post(
            postId = args.postId,
            modifier = Modifier,
            popUpScreen = { appState.popUp() }
        )
    }

    composable<AccountsScreen> {
        AccountsScreen(
            modifier = Modifier,
            openScreen = { appState.navigate(it) },
            clearAndNavigate = { appState.clearAndNavigate(it) })
    }

    composable<LoginScreen> {
        LoginScreen(
            modifier = Modifier,
            openAndPopUp = { screen1, screen2 ->
                appState.navigateAndPopUp(screen1, screen2)
            },
            openScreen = {
                appState.navigate(it)
            },
            clearAndNavigate = {
                appState.clearAndNavigate(it)
            })
    }

    composable<RegisterScreen> {
        RegisterScreen(
            modifier = Modifier,
            openAndPopUp = { screen1, screen2 ->
                appState.navigateAndPopUp(screen1, screen2)
            },
            clearAndNavigate = {
                appState.clearAndNavigate(it)
            })
    }

    composable<ConversationList> {
        ChatListScreen() {
            appState.navigate(it)
        }
    }

    composable<Chat> {
        val args = it.toRoute<Chat>()
        ChatScreen(conversationId = args.conversationId, popUp = { appState.popUp() })
    }
}


@Serializable
object FeedScreen

@Serializable
object AccountsScreen

@Serializable
object ConversationList

@Serializable
data class Chat(val conversationId: String)

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
data class PostScreen(val postId: String)


