package com.saswat10.jetnetwork.domain.repository

import com.saswat10.jetnetwork.domain.models.Conversation
import com.saswat10.jetnetwork.domain.models.Message
import com.saswat10.jetnetwork.domain.models.User
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val conversationList: Flow<List<Conversation>>

    suspend fun createChatUser(user: User)
    suspend fun getUsers(searchQuery: String): Flow<List<User>>
    suspend fun createNewConversation(targetUser: User): Conversation?
    suspend fun loadConversation(conversationId: String): Flow<List<Message>>
    suspend fun sendMessage(conversation: Conversation, message: Message)
    suspend fun updateMessage(conversation: Conversation, message: Message)
    suspend fun deleteMessage(conversation: Conversation, message: Message)

}