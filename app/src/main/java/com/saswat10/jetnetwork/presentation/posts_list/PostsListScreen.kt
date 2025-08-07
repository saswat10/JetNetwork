package com.saswat10.jetnetwork.presentation.posts_list

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
import com.saswat10.jetnetwork.presentation.feed.FeedItem

@Composable
fun PostListScreen(viewModel: PostsListViewModel = hiltViewModel()){
    LaunchedEffect(Unit) { viewModel.initialize() }
    val posts by viewModel.postWithLikes.collectAsStateWithLifecycle(emptyList())

    Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(10.dp)) {
        LazyColumn {
            items(posts, key = {it.post.id}){postItem ->
                FeedItem(postItem, {}, {viewModel.toggleLike(postItem.post.id)}, {})
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}
