package com.saswat10.jetnetwork.presentation.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.google.firebase.Timestamp
import com.saswat10.jetnetwork.domain.models.Conversation
import com.saswat10.jetnetwork.domain.models.Message
import com.saswat10.jetnetwork.domain.models.User
import com.saswat10.jetnetwork.utils.formatChatDate

@Composable
fun ConversationItem(
    currentUserId: String,
    conversation: Conversation,
    navigateToConservation: () -> Unit
) {

    val lastMessage = conversation.lastMessage.content
    val lastMessageSenderId = conversation.lastMessage.senderId
    val isCurrentUserSender = (lastMessageSenderId == currentUserId)
    val participant = conversation.participants.first {
        it.id != currentUserId
    }

    ListItem(
        leadingContent = {
            if(participant.photoUrl.isBlank()){
                Icon(Icons.Default.AccountCircle, null, Modifier.size(44.dp))
            }else {
                AsyncImage(
                    model = participant.photoUrl,
                    contentDescription = participant.displayName,
                    modifier = Modifier.size(40.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        headlineContent = {
            Text(participant.displayName.ifBlank { "Anonymous User" })
        },
        supportingContent = {
            if(isCurrentUserSender){
                Text(
                    text = "You: $lastMessage",
                    style = MaterialTheme.typography.labelMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }else{
                Text(
                    text = lastMessage,
                    style = MaterialTheme.typography.labelMedium,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }

        },
        trailingContent = {
            Text(formatChatDate(conversation.lastMessage.timestamp))
        },
        modifier = Modifier.clickable{
            navigateToConservation()
        }
        // TODO: adding badge for unread messages, and conditional date format
    )
}


@Preview(apiLevel = 34, showSystemUi = false, showBackground = false)
@Composable
fun ConversationPreview1() {
    ConversationItem(
        currentUserId = "alice",
        conversation = Conversation(
            id = "alice_bob",
            participantIds = listOf("alice", "bob"),
            participants = listOf(User("alice", displayName = "Alice Smith"), User(id = "bob", displayName = "Bob Smith")),
            lastMessage = Message(
                id = "message",
                senderId = "alice",
                senderName = "alice denver",
                messageType = "text",
                content = "Coffee at 7?",
                timestamp = Timestamp.now()
            )
        ),
        navigateToConservation = {}
    )
}

@Preview(apiLevel = 34, showSystemUi = false, showBackground = false)
@Composable
fun ConversationPreview2() {
    ConversationItem(
        currentUserId = "alice",
        conversation = Conversation(
            id = "alice_bob",
            participantIds = listOf("alice", "bob"),
            participants = listOf(User("alice", displayName = "Alice Smith"), User(id = "rachel", displayName = "Rachel Smith")),
            lastMessage = Message(
                id = "message",
                senderId = "bob",
                senderName = "Bob Smith",
                messageType = "text",
                content = "Yeah!, Let's meet at CoffeeHouse",
                timestamp = Timestamp.now()
            )
        ),
        navigateToConservation = {}
    )
}