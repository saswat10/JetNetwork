package com.saswat10.jetnetwork.presentation.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saswat10.jetnetwork.PostScreen
import com.saswat10.jetnetwork.ProvideJNTopAppBarTitle
import com.saswat10.jetnetwork.utils.DEFAULT_POST_ID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(viewModel: FeedViewModel = hiltViewModel(), openScreen: (Any) -> Unit) {
    LaunchedEffect(Unit) { viewModel.initialize() }
    val posts by viewModel.postWithLikes.collectAsStateWithLifecycle(emptyList())
    val comments by viewModel.comments.collectAsStateWithLifecycle(emptyList())
    val showBottomSheet by viewModel.showModalSheet.collectAsStateWithLifecycle()
    val showEditCommentDialog by viewModel.showDialog.collectAsStateWithLifecycle()
    val comment by viewModel.comment.collectAsStateWithLifecycle()

    val scrollstate = rememberLazyListState()

    // TODO: Add edit post in the feed item
    // TODO: Also add delete post in the feed item
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ProvideJNTopAppBarTitle {
            Text("JetNetwork", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Column(modifier = Modifier
            .fillMaxSize()) {
            LazyColumn(
                userScrollEnabled = true,
                state = scrollstate
            ) {
                item {

                }
                items(posts, key = { it.post.id }) { postItem ->
                    FeedItem(
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = 10.dp),
                        postWithLikes = postItem,
                        getComments = {
                            viewModel.showModalSheet()
                            viewModel.getComments(postItem.post.id)
                        },
                        toggleLike = { viewModel.toggleLike(postItem.post.id) },
                        toggleBookMark = {}
                    )
                    Spacer(Modifier.height(10.dp))
                }

                item { Spacer(Modifier.height(60.dp)) }
            }

        }
        FloatingActionButton(onClick = {
            openScreen(PostScreen(DEFAULT_POST_ID))
        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(bottom = 10.dp, end = 16.dp)) {
            Icon(Icons.Default.Add, "Add")
        }
    }
    if (showBottomSheet) {
        CommentBottomSheet(
            addComment = { viewModel.addComment(it) },
            onDismiss = { viewModel.onDismiss() },
            comments = comments,
            currentUserId = viewModel.currentUserId,
            onDelete = { viewModel.deleteComment(it) },
            showEditDialog = { commentId ->
                viewModel.getComment(commentId)
                viewModel.showEditDialog()
            }
        )
    }

    if (showEditCommentDialog) {
        EditCommentDialog(
            onDismiss = { viewModel.onDismissDialog() },
            value = comment.comment,
            updateValue = { viewModel.updateComment(it) },
            confirmButton = { viewModel.updateCommentClick() }
        )
    }
}


// TODO: add comment logic in order prevent function drilling
// TODO: make the add comment in such a way so that we can update any previous comment of the user