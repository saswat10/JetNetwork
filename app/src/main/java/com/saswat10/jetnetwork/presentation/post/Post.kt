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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saswat10.jetnetwork.ProvideJNTopAppBarAction
import com.saswat10.jetnetwork.ProvideJNTopAppBarNavigationIcon
import com.saswat10.jetnetwork.ProvideJNTopAppBarTitle
import com.saswat10.jetnetwork.R
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

    LaunchedEffect(Unit) {
        viewModel.initialize(postId)
    }

    ProvideJNTopAppBarNavigationIcon {
        IconButton(onClick = {popUpScreen()}) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
        }
    }
    ProvideJNTopAppBarAction {
        Button(onClick = { viewModel.savePost(popUpScreen) }, modifier = Modifier.padding(end = 10.dp)) {
            Text("Post")
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
                modifier = Modifier.padding(24.dp, 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                    Icon(painterResource(R.drawable.add_photo), "Add Photos", Modifier.size(30.dp))
                    Icon(painterResource(R.drawable.add_video), "Add Video", Modifier.size(30.dp))
                }

            }
        }

    }
}

//@Preview(apiLevel = 34)
//@Composable
//fun PostPreview() {
//
//    Post(postId = DEFAULT_POST_ID, modifier = Modifier.fillMaxSize())
//}