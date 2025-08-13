package com.saswat10.jetnetwork.presentation.chat.chat

import androidx.lifecycle.viewModelScope
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.models.Conversation
import com.saswat10.jetnetwork.domain.models.Message
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.ChatRepository
import com.saswat10.jetnetwork.utils.formatChatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    authRepository: AuthRepository
) : JNViewModel() {

    val currentUserId = authRepository.currentUserId

    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation = _conversation.asStateFlow()
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val groupedMessages: StateFlow<Map<String, List<Message>>> =
        _messages
            .map { messages ->
                messages.groupBy { message ->
                    formatChatDate(message.timestamp)
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())


    val participant = _conversation.map { conversation ->
        conversation?.participants?.first { user ->
            user.id != authRepository.currentUserId
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(1000), null)

    fun loadConversation(conversationId: String) {
        launchCatching {
            _conversation.value = chatRepository.getConversationById(conversationId)
        }
    }

    fun loadConversationMessages(conversationId: String) {
        launchCatching {
            chatRepository.loadConversation(conversationId).collect { messages ->
                _messages.update { messages }
            }
        }
    }

    fun addMessage(conversationId: String, content: String) {
        launchCatching {
            val message = Message(messageType = "text", content = content)
            chatRepository.sendMessage(conversationId, message)
        }
    }
}