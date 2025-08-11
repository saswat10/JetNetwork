package com.saswat10.jetnetwork.presentation.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.domain.models.Comment
import com.saswat10.jetnetwork.utils.formatName
import com.saswat10.jetnetwork.utils.formattedTime

class FeedCommentSheet {
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentBottomSheet(
    addComment: (String) -> Unit,
    comments: List<Comment>,
    onDelete: (String) -> Unit,
    onDismiss: () -> Unit,
    currentUserId: String,
    showEditDialog: (String) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var comment by remember { mutableStateOf("") }


    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = { onDismiss() }
    ) {
        Text(
            text = "Comments", modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 8.dp), textAlign = TextAlign.Center
        )

        HorizontalDivider()

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
                    addComment(comment)
                    comment = ""
                }, modifier = Modifier.padding(4.dp)) {
                    Icon(Icons.AutoMirrored.Filled.Send, "Send")
                }
            }
        )


        LazyColumn {
            items(comments, key = { it.id }) { comment ->
                CommentCard(
                    modifier = Modifier.animateItem(),
                    comment,
                    currentUserId,
                    onDelete = { onDelete(comment.id) },
                    showEditDialog = { showEditDialog(comment.id) })

            }
        }

    }
}


// TODO : add delete and update comment for matching userid
@Composable
fun CommentCard(
    modifier: Modifier,
    comment: Comment,
    currentUserId: String,
    onDelete: () -> Unit,
    showEditDialog: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp, 16.dp, 6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(12.dp, 8.dp)
        ) {
            Icon(Icons.Default.AccountCircle, comment.userId, Modifier.size(30.dp))
            Column(Modifier.weight(1f)) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(formatName(comment.username), style = MaterialTheme.typography.labelLarge)
                    Text(
                        formattedTime(comment.createdAt),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Text(
                    comment.comment,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(0.dp, 4.dp)
                )
            }


            if (comment.userId == currentUserId) {
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.offset(x = (12).dp, y = (-8).dp)) {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Rounded.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Edit,
                                    "edit",
                                    Modifier.size(18.dp)
                                )
                            },
                            text = { Text("Edit") },
                            onClick = { showEditDialog() }
                        )
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    "delete",
                                    Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                            onClick = { onDelete() }
                        )
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCommentDialog(
    onDismiss: () -> Unit,
    value: String,
    updateValue: (String) -> Unit,
    confirmButton: () -> Unit
) {
    AlertDialog(
        title = { Text(stringResource(R.string.update_comment)) },
        text = {
            Column {
                OutlinedTextField(
                    value = value,
                    onValueChange = { updateValue(it) }
                )
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        confirmButton = {
            Button(onClick = {
                confirmButton()
                onDismiss()
            }) {
                Text(text = stringResource(R.string.update))
            }
        },
        onDismissRequest = { onDismiss() }
    )
}

//@Preview(
//    apiLevel = 34, showBackground = false, showSystemUi = false,
//    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
//)
//@Composable
//fun CommentCardPreview() {
//    CommentCard(Comment(comment = "This is my first comment, checking in the preview, long text can get to the next line...👉👈"))
//}