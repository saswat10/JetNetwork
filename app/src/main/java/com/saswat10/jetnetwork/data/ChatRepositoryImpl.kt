package com.saswat10.jetnetwork.data

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.saswat10.jetnetwork.domain.models.Conversation
import com.saswat10.jetnetwork.domain.models.Message
import com.saswat10.jetnetwork.domain.models.User
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.ChatRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ChatRepository {

    override val conversationList: Flow<List<Conversation>>
        get() = authRepository.currentUser.flatMapLatest { user ->
            Firebase.firestore
                .collection(CONVERSATION_COLLECTION)
                .whereArrayContains(PARTICIPANT_IDS_FIELD, user?.id!!)
                .dataObjects()
        }

    override suspend fun createChatUser(user: User) {
        val user = authRepository.getUserProfile()
        Firebase.firestore
            .collection(USERS_COLLECTION)
            .document(user.id)
            .set(user)
            .await()
    }


    override suspend fun getUsers(searchQuery: String): Flow<List<User>> {
        return authRepository.currentUser.flatMapLatest { user ->
            Firebase.firestore
                .collection(USERS_COLLECTION)
                .whereGreaterThanOrEqualTo(DISPLAY_NAME_FIELD, searchQuery)
                .whereLessThanOrEqualTo(DISPLAY_NAME_FIELD, searchQuery+'\u8FFF')
                .dataObjects()
        }
    }

    override suspend fun createNewConversation(targetUser: User): Conversation? {
        val currentUser = authRepository.getUserProfile()
        val newConversation = Conversation(
            participantIds = listOf(currentUser.id, targetUser.id),
            participants = listOf(currentUser, targetUser)
        )

        val docRef = Firebase.firestore
            .collection(CONVERSATION_COLLECTION)
            .add(newConversation)
            .await()

        return docRef.get().await().toObject<Conversation>()
    }

    override suspend fun loadConversation(conversationId: String): Flow<List<Message>> {
        return authRepository.currentUser.flatMapLatest {
            Firebase.firestore
                .collection(CONVERSATION_COLLECTION)
                .document(conversationId)
                .collection(MESSAGES_SUBCOLLECTION)
                .orderBy(TIMESTAMP_FIELD, Query.Direction.DESCENDING)
                .dataObjects()
        }
    }

    override suspend fun sendMessage(conversation: Conversation, message: Message) {
        val messageWithUserData = message.copy(
            senderId = authRepository.currentUserId,
            senderName = authRepository.currentUserName,
            timestamp = Timestamp.now()
        )


        Firebase.firestore
            .collection(CONVERSATION_COLLECTION)
            .document(conversation.id)
            .collection(MESSAGES_SUBCOLLECTION)
            .add(messageWithUserData)
            .await()
    }

    override suspend fun updateMessage(conversation: Conversation, message: Message) {
        val messageWithUserData = message.copy(
            senderId = authRepository.currentUserId,
            senderName = authRepository.currentUserName,
            timestamp = Timestamp.now()
        )


        Firebase.firestore
            .collection(CONVERSATION_COLLECTION)
            .document(conversation.id)
            .collection(MESSAGES_SUBCOLLECTION)
            .document(messageWithUserData.id)
            .set(messageWithUserData)
            .await()
    }

    override suspend fun deleteMessage(conversation: Conversation, message: Message) {
        Firebase.firestore
            .collection(CONVERSATION_COLLECTION)
            .document(conversation.id)
            .collection(MESSAGES_SUBCOLLECTION)
            .document(message.id)
            .delete()
            .await()
    }

    companion object {
        private const val CONVERSATION_COLLECTION = "conversations"
        private const val PARTICIPANT_IDS_FIELD = "participantIds"
        private const val USERS_COLLECTION = "users"
        private const val DISPLAY_NAME_FIELD = "displayName"
        private const val MESSAGES_SUBCOLLECTION = "messages"
        private const val TIMESTAMP_FIELD = "timestamp"
    }
}