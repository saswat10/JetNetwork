package com.saswat10.jetnetwork.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.saswat10.jetnetwork.AccountsScreen
import com.saswat10.jetnetwork.FeedScreen

const val DEFAULT_POST_ID = "-1"
const val POST_ID = "postId"

sealed class NavDestination(val title: String, val route: Any, val icon: ImageVector){
    object Feed: NavDestination("Feed", FeedScreen, Icons.Default.Home)
    object Accounts: NavDestination("Account", AccountsScreen, Icons.Default.Person)
}