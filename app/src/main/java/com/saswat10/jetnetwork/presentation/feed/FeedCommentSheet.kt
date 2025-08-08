package com.saswat10.jetnetwork.presentation.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.domain.models.Comment
import com.saswat10.jetnetwork.utils.formatName
import com.saswat10.jetnetwork.utils.formattedTime

class FeedCommentSheet {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheetIcon(
    addComment: (String) -> Unit,
    getComments: ()->Unit,
    comments: List<Comment>
) {

    var showbottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var comment by remember { mutableStateOf("") }

    Icon(
        painter = painterResource(R.drawable.comment_24px),
        "Comment",
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .clickable {
                getComments()
                showbottomSheet = true
            }
            .padding(4.dp),
        tint = Color.Gray
    )

    if (showbottomSheet) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxHeight(),
            sheetState = sheetState,
            onDismissRequest = { showbottomSheet = false }
        ) {
            Text(
                text = "Comments", modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 0.dp, 16.dp, 8.dp), textAlign = TextAlign.Center
            )

            HorizontalDivider()

            OutlinedTextField(
                value = comment,
                onValueChange = {comment = it},
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
                        addComment(comment)
                    }, modifier = Modifier.padding(4.dp)) {
                        Icon(Icons.AutoMirrored.Filled.Send, "Send")
                    }
                }
            )


            LazyColumn {
                items(comments,key = {it.id}){comment->
                    CommentCard(comment)

                }
            }

        }
    }
}


// TODO : add delete and update comment for matching userid
@Composable
fun CommentCard(comment: Comment) {
    Card(modifier = Modifier.fillMaxWidth().padding(16.dp, 4.dp, 16.dp, 6.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(8.dp, 8.dp)
        ) {
            Icon(Icons.Default.AccountCircle, comment.userId)
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(formatName(comment.username), style = MaterialTheme.typography.labelLarge)
                    Text(
                        formattedTime(comment.createdAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }


                Text(
                    comment.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(0.dp, 4.dp)
                )
            }
        }

    }
}

@Preview(apiLevel = 34, showBackground = false, showSystemUi = false,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
@Composable
fun CommentCardPreview() {
    CommentCard(Comment(comment = "This is my first comment, checking in the preview, long text can get to the next line...👉👈"))
}