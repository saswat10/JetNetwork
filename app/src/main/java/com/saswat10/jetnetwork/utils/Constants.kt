package com.saswat10.jetnetwork.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.saswat10.jetnetwork.AccountsScreen
import com.saswat10.jetnetwork.ConversationList
import com.saswat10.jetnetwork.FeedScreen
import com.saswat10.jetnetwork.R

const val DEFAULT_POST_ID = "-1"
const val POST_ID = "postId"

sealed class NavDestination(val title: String, val route: Any, val icon: Int){
    object Feed: NavDestination("Feed", FeedScreen, R.drawable.home_24px)
    object Conversation: NavDestination("Chats", ConversationList, R.drawable.forum_24px)
    object Accounts: NavDestination("Account", AccountsScreen, R.drawable.person_24px)
}