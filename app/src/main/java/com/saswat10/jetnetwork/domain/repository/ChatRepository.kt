package com.saswat10.jetnetwork.domain.repository

import com.saswat10.jetnetwork.domain.models.Conversation
import com.saswat10.jetnetwork.domain.models.Message
import com.saswat10.jetnetwork.domain.models.User
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val conversationList: Flow<List<Conversation>>

    suspend fun createChatUser()
    suspend fun getUsers(searchQuery: String): List<User>
    suspend fun createNewConversation(targetUser: User): Conversation?
    suspend fun conversationExists(targetUser: User): Boolean
    suspend fun getConversation(targetUser: User): Conversation
    suspend fun loadConversation(conversationId: String): Flow<List<Message>>
    suspend fun sendMessage(conversationId: String, message: Message)
    suspend fun updateMessage(conversation: Conversation, message: Message)
    suspend fun deleteMessage(conversation: Conversation, message: Message)

}