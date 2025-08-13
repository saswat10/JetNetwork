package com.saswat10.jetnetwork.utils

import com.saswat10.jetnetwork.ui.AccountsScreen
import com.saswat10.jetnetwork.ui.ConversationList
import com.saswat10.jetnetwork.ui.FeedScreen
import com.saswat10.jetnetwork.R

const val DEFAULT_POST_ID = "-1"
const val POST_ID = "postId"

sealed class NavDestination(val title: String, val route: Any, val icon: Int){
    object Feed: NavDestination("Feed", FeedScreen, R.drawable.home_24px)
    object Conversation: NavDestination("Chats", ConversationList, R.drawable.forum_24px)
    object Accounts: NavDestination("Account", AccountsScreen, R.drawable.person_24px)
}