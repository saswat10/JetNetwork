package com.saswat10.jetnetwork.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.referentialEqualityPolicy
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.saswat10.jetnetwork.JNViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JNTopAppBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    backStackEntry?.let { entry ->

        val viewModel: JNTopAppBarViewModel = viewModel(
            viewModelStoreOwner = entry,
            initializer = { JNTopAppBarViewModel() }
        )

        CenterAlignedTopAppBar(
            title = {
                AnimatedContent(
                    targetState = viewModel.title,
                    label = "TopAppBarTitleAnimation",
                ) { titleContent ->
                    titleContent()
                }
            },
            navigationIcon = {
                AnimatedContent(
                    targetState = viewModel.navigationIcon,
                    label = "TopAppBarNavigationIconAnimation"
                ) { navigationIconContent ->
                    navigationIconContent()
                }
            },
            actions = {
                AnimatedContent(
                    targetState = viewModel.actions,
                    label = "TopAppBarActionsAnimation"
                ) { actionsContent ->
                    actionsContent()
                }
            },
            scrollBehavior = scrollBehavior,
            modifier = modifier,
            colors = TopAppBarDefaults.largeTopAppBarColors()
        )
    }
}


private class JNTopAppBarViewModel : JNViewModel() {
    var title by mutableStateOf<@Composable () -> Unit>({}, referentialEqualityPolicy())
    var navigationIcon by mutableStateOf<@Composable () -> Unit>({}, referentialEqualityPolicy())
    var actions by mutableStateOf<@Composable () -> Unit>({}, referentialEqualityPolicy())
}


@Composable
fun ProvideJNTopAppBarTitle(title: @Composable () -> Unit) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: JNTopAppBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { JNTopAppBarViewModel() }
        )

        LaunchedEffect(title) {
            viewModel.title = title
        }
    }
}

@Composable
fun ProvideJNTopAppBarNavigationIcon(navigationIcon: @Composable () -> Unit) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: JNTopAppBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { JNTopAppBarViewModel() }
        )

        LaunchedEffect(navigationIcon) {
            viewModel.navigationIcon = navigationIcon
        }
    }
}

@Composable
fun ProvideJNTopAppBarAction(actions: @Composable () -> Unit) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    (viewModelStoreOwner as? NavBackStackEntry)?.let { owner ->
        val viewModel: JNTopAppBarViewModel = viewModel(
            viewModelStoreOwner = owner,
            initializer = { JNTopAppBarViewModel() }
        )

        LaunchedEffect(actions) {
            viewModel.actions = actions
        }
    }
}