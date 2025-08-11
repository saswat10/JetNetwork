package com.saswat10.jetnetwork.presentation.chat.chat_list

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.saswat10.jetnetwork.FeedScreen
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.models.User
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

sealed interface SearchState {
    object Empty : SearchState
    data class userQuery(val query: String) : SearchState
}

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val chatRepository: ChatRepository
) : JNViewModel() {


    init {
        createChatUser()
    }

    val basicTextState = TextFieldState()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private val searchTextState: StateFlow<SearchState> = snapshotFlow { basicTextState.text }
        .debounce(500)
        .mapLatest { if (it.isBlank()) SearchState.Empty else SearchState.userQuery(it.toString()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2_000),
            initialValue = SearchState.Empty
        )

    val conversationList = chatRepository.conversationList
    val currentUserId = authRepository.currentUserId
    private val _userList = MutableStateFlow<List<User>>(emptyList())
    val userList = _userList.asStateFlow()


    private fun createChatUser() {
        launchCatching {
            chatRepository.createChatUser()
        }
    }

    fun loadConversation(targetUser: User, openScreen:(Any)->Unit){
        launchCatching {
            val result = chatRepository.conversationExists(targetUser)
            if(result){
                openScreen(FeedScreen)
                val conversation = chatRepository.getConversation(targetUser)
                println(conversation)
            }else{
                chatRepository.createNewConversation(targetUser)
                openScreen(FeedScreen)
            }
        }
    }

    @OptIn(FlowPreview::class)
    fun searchUsers(newString: String) {
        launchCatching {
            searchTextState.collectLatest { searchState ->
                when (searchState) {
                    is SearchState.Empty -> _userList.update { emptyList<User>() }
                    is SearchState.userQuery -> _userList.update {
                        chatRepository.getUsers(searchState.query)
                    }
                }
            }
        }
    }
}
