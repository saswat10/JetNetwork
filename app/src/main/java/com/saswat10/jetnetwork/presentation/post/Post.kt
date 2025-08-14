package com.saswat10.jetnetwork.presentation.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.ui.ProvideJNTopAppBarAction
import com.saswat10.jetnetwork.ui.ProvideJNTopAppBarNavigationIcon
import com.saswat10.jetnetwork.utils.DEFAULT_POST_ID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Post(
    postId: String,
    modifier: Modifier,
    viewModel: PostViewModel = hiltViewModel(),
    popUpScreen: () -> Unit
) {

    val post by viewModel.post.collectAsStateWithLifecycle()
    val user by viewModel.user.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initialize(postId)
    }

    ProvideJNTopAppBarNavigationIcon {
        IconButton(onClick = { popUpScreen() }) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
        }
    }
    ProvideJNTopAppBarAction {
        Row(
            modifier = Modifier.padding(end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            InputChip(
                onClick = { viewModel.toggleVisibility() },
                label = { if (post.private) Text("Private") else Text("Public") },
                enabled = !user.anonymous,
                selected = !post.private,
                leadingIcon = {
                    if (post.private) Icon(
                        painterResource(R.drawable.visibility_off_24px),
                        null,
                        modifier = Modifier.size(16.dp)
                    )
                    else Icon(
                        painterResource(R.drawable.visibility_24px),
                        null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
            if(postId != DEFAULT_POST_ID) {
                IconButton(onClick = { viewModel.deletePost(postId, popUpScreen) }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        tint = MaterialTheme.colorScheme.error,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        LazyColumn(modifier = modifier.imePadding()) {
            item {
                OutlinedTextField(
                    value = post.title,
                    onValueChange = { viewModel.updateTitle(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 2.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    textStyle = MaterialTheme.typography.titleLarge + TextStyle(fontWeight = FontWeight.Bold),
                    placeholder = {
                        Text(
                            text = "Title",
                            style = MaterialTheme.typography.titleLarge + TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                )

                HorizontalDivider()

                OutlinedTextField(
                    value = post.content,
                    onValueChange = { viewModel.updateContent(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 2.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    minLines = 16,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = {
                        Text(
                            text = "Body",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.offset(y = (-4).dp)
                        )
                    }
                )
            }

        }
        Surface(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Row(
                modifier = Modifier.padding(20.dp, 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.savePost(popUpScreen) },
                ) {
                    Text("Post")
                }


            }
        }

    }
}
