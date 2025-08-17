package com.saswat10.jetnetwork.presentation.chat.chat

import androidx.lifecycle.viewModelScope
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.models.Conversation
import com.saswat10.jetnetwork.domain.models.Message
import com.saswat10.jetnetwork.domain.models.OnlineStatus
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.ChatRepository
import com.saswat10.jetnetwork.domain.repository.OnlineStatusRepository
import com.saswat10.jetnetwork.utils.formatChatDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val onlineStatusRepository: OnlineStatusRepository,
    authRepository: AuthRepository
) : JNViewModel() {

    val currentUserId = authRepository.currentUserId

    private val _conversation = MutableStateFlow<Conversation?>(null)
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

    private val _status = MutableStateFlow(OnlineStatus())
    val status = _status.asStateFlow()

    init{
        observerUserStatus()
    }
    fun loadConversation(conversationId: String) {
        launchCatching {
            _conversation.value = chatRepository.getConversationById(conversationId)
        }
    }

    fun observerUserStatus(){
        launchCatching {
            participant.collectLatest { participantUser ->
                // Check if the participant is not null before proceeding
                if (participantUser != null) {
                    onlineStatusRepository.getOnlineStatus(participantUser.id).collectLatest { status ->
                        _status.value = status
                    }
                } else {
                    // Set a default status if the participant is not yet loaded
                    _status.value = OnlineStatus()
                }
            }
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