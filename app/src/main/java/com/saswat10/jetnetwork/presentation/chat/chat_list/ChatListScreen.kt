package com.saswat10.jetnetwork.presentation.chat.chat_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saswat10.jetnetwork.ui.Chat
import com.saswat10.jetnetwork.ui.ProvideJNTopAppBarAction
import com.saswat10.jetnetwork.ui.ProvideJNTopAppBarTitle
import com.saswat10.jetnetwork.presentation.chat.components.ConversationItem
import com.saswat10.jetnetwork.presentation.chat.components.SimpleSearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(viewModel: ChatListViewModel = hiltViewModel(), openScreen:(Any)->Unit) {

    val conversationList by viewModel.conversationList.collectAsStateWithLifecycle(initialValue = emptyList())
    val currentUserId = viewModel.currentUserId
    val basicTextField = viewModel.basicTextState
    val users by viewModel.userList.collectAsStateWithLifecycle()
    var showSearch by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadConversations()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(showSearch) {
            SimpleSearchBar(
                textFieldState = basicTextField,
                onSearch = { viewModel.searchUsers(it) },
                searchResults = users,
            ) { user ->
                viewModel.loadConversation(user) {
                    println(it)
                    println(user)
                }
            }
        }

        LazyColumn() {
            item {

            }
            items(conversationList, { it.id }) { conversation ->
                ConversationItem(
                    currentUserId = currentUserId,
                    conversation = conversation,
                    navigateToConservation = {
                        openScreen(Chat(conversation.id))
                    }
                )
                HorizontalDivider()
            }
        }

        ProvideJNTopAppBarTitle {
            Text(
                "Messages",
                style = MaterialTheme.typography.titleLarge,
            )
        }

        ProvideJNTopAppBarAction {
            IconButton(onClick = {showSearch = !showSearch} ) {
                Icon(Icons.Default.Search, "Search")
            }
        }
    }
}