package com.saswat10.jetnetwork.presentation.chat.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saswat10.jetnetwork.presentation.chat.components.MessageItem

@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel(), conversationId: String) {
    LaunchedEffect(Unit) {
        viewModel.loadConversationMessages(conversationId)
    }

//    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val messagesWithDate by viewModel.groupedMessages.collectAsStateWithLifecycle()
    val currentUserId = viewModel.currentUserId
    var comment by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
//        Row(Modifier.fillMaxWidth().background(Color.Red)) {
//            Row(
//                Modifier.padding(20.dp, 20.dp),
//                horizontalArrangement = Arrangement.spacedBy(10.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(Icons.Default.AccountCircle, null, Modifier.size(40.dp))
//                Text("Name of th", style = MaterialTheme.typography.titleLarge)
//            }
//        }
        LazyColumn(
            reverseLayout = true,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(1f)
        ) {


            messagesWithDate.forEach { (date, messages) ->
                items(messages, key = { it.id }) { message ->
                    MessageItem(message, currentUserId, Modifier.animateItem())
                }
                item {
                    Text(
                        date,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }
        }

        // TODO: Add image upload options here
        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 8.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            placeholder = {
                Text(
                    text = "Say something!",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            shape = CircleShape,
            trailingIcon = {
                FilledIconButton(onClick = {
                    viewModel.addMessage(conversationId, comment)
                    comment = ""
                }, modifier = Modifier.padding(4.dp)) {
                    Icon(Icons.AutoMirrored.Filled.Send, "Send")
                }
            },
        )
    }

}