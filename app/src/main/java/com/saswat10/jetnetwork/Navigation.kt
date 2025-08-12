package com.saswat10.jetnetwork

import kotlinx.serialization.Serializable

sealed class Navigation {

    @Serializable
    object FeedScreen

    @Serializable
    object AccountsScreen

    @Serializable
    object ConversationList

    @Serializable
    data class ChatList(val conversationId: String)

    @Serializable
    object LoginScreen

    @Serializable
    object RegisterScreen

    @Serializable
    data class PostScreen(val postId: String)

}