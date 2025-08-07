package com.saswat10.jetnetwork.presentation.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FeedScreen(viewModel: FeedViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) { viewModel.initialize() }
    val posts by viewModel.postWithLikes.collectAsStateWithLifecycle(emptyList())
    val comments by viewModel.comments.collectAsStateWithLifecycle(emptyList())

    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(10.dp)) {
        LazyColumn {
            items(posts, key = { it.post.id }) { postItem ->
                FeedItem(
                    postItem,
                    comments,
                    getComments = { viewModel.getComments(postItem.post.id) },
                    addComment = {},
                    { viewModel.toggleLike(postItem.post.id) },
                    {})
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}


// TODO: add comment logic in order prevent function drilling
// TODO: make the add comment in such a way so that we can update any previous comment of the user