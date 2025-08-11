package com.saswat10.jetnetwork.presentation.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.saswat10.jetnetwork.domain.models.Message
import com.saswat10.jetnetwork.utils.localTimeString
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun MessageItem(message: Message, currentUserId: String, modifier: Modifier = Modifier) {

    val userColor: CardColors = CardColors(
        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    )

    val friendColor: CardColors = CardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        disabledContentColor = MaterialTheme.colorScheme.onSurface
    )

    val userShape = MaterialTheme.shapes.medium.copy(bottomEnd = CornerSize(0.dp))
    val friendShape = MaterialTheme.shapes.medium.copy(bottomStart = CornerSize(0.dp))

    val alignment: Alignment =
        if (message.senderId == currentUserId) Alignment.CenterEnd else Alignment.CenterStart

    Box(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .align(alignment)
                .widthIn(min = IntrinsicSize.Min.ordinal.dp, max = 300.dp)
                .padding(8.dp, 2.dp),
            content = {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(message.content)
                    Text(
                        localTimeString(message.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier= if(message.senderId == currentUserId) Modifier.align(Alignment.End) else Modifier.align(Alignment.Start)
                    )
                }
            },
            colors = if (message.senderId == currentUserId) userColor else friendColor,
            shape = if (message.senderId == currentUserId) userShape else friendShape,
        )
    }
}


@Preview(apiLevel = 34, showBackground = false, showSystemUi = false)
@Composable
fun MessagePreview() {
    Column(modifier = Modifier.fillMaxWidth()) {
        MessageItem(
            currentUserId = "alice", message = Message(
                "id1",
                senderId = "alice",
                senderName = "Alice Smith",
                messageType = "text",
                content = "You up for a Game Night",
                timestamp = Timestamp.now(),
            )
        )
        MessageItem(
            currentUserId = "alice", message = Message(
                "id2",
                senderId = "bob",
                senderName = "Bob Denver",
                messageType = "text",
                content = "Who all are coming over?",
                timestamp = Timestamp.now(),
            )
        )
        MessageItem(
            currentUserId = "alice", message = Message(
                "id3",
                senderId = "alice",
                senderName = "Alice Smith",
                messageType = "text",
                content = "George, Katie, Rachel & Oscar",
                timestamp = Timestamp.now(),
            )
        )
        MessageItem(
            currentUserId = "alice", message = Message(
                "id3",
                senderId = "alice",
                senderName = "Alice Smith",
                messageType = "text",
                content = "Let's have fun tonight..🤗",
                timestamp = Timestamp.now(),
            )
        )
    }
}

