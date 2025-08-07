package com.saswat10.jetnetwork.presentation.posts_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saswat10.jetnetwork.domain.models.Post
import com.saswat10.jetnetwork.utils.formattedTime

@Composable
fun PostListScreen(viewModel: PostsListViewModel = hiltViewModel()){
    LaunchedEffect(Unit) { viewModel.initialize() }
    val posts by viewModel.posts.collectAsStateWithLifecycle(emptyList())
    val postFlow  by viewModel.postFlow.collectAsStateWithLifecycle()

    val title by viewModel.title.collectAsStateWithLifecycle()
    val content by viewModel.content.collectAsStateWithLifecycle()


    Column(modifier = Modifier.fillMaxSize().systemBarsPadding().padding(10.dp)) {
        OutlinedTextField(value = title, onValueChange = {viewModel.updateTitle(it)})
        OutlinedTextField(value = content, onValueChange = {viewModel.updateContent(it)})

        Button(onClick = {viewModel.createNewPost()}) { Text("Create New Post")}
        LazyColumn {
            items(postFlow, key = {it.id}){postItem ->
                PostItem(post = postItem)
            }
        }
    }
}


@Composable
fun PostItem(
    post: Post,
){
    Card(modifier = Modifier.padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
//                .clickable { onActionClick(note.id) }
        ) {
            Text(
                text = post.getTitleString() + "    " + formattedTime(post.createdAt),
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
