package com.saswat10.jetnetwork.presentation.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.saswat10.jetnetwork.domain.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<User>,
    createConversation: (User) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                isTraversalGroup = true
            }) {
        SearchBar(
            windowInsets = WindowInsets(top = 0),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = {
                SearchBarDefaults.InputField(
                    leadingIcon = {
                        Icon(Icons.Default.Search, "Search")
                    },
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Search Users") }
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Display search results in a scrollable column
            Column(Modifier.verticalScroll(rememberScrollState())) {
                searchResults.forEach { result ->
                    UserSearchItem(result) {
                        createConversation(result)
                    }
                }
            }
        }
    }
}

@Composable
fun UserSearchItem(user: User, onClick: () -> Unit) {
    ListItem(
        leadingContent = {
            if(user.photoUrl.isBlank()){
                Icon(Icons.Default.AccountCircle, null, Modifier.size(40.dp))
            }else {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = user.displayName,
                    Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
        },
        headlineContent = { Text(user.displayName) },
        trailingContent = { TextButton({ onClick() }, content = { Text("Message") }) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp, 8.dp, 10.dp, 0.dp)
            .clip(MaterialTheme.shapes.medium)
    )
}


@Preview(apiLevel = 34, showBackground = false, showSystemUi = false)
@Composable
fun SearchBarPreview() {
    SimpleSearchBar(
        textFieldState = TextFieldState(""),
        onSearch = {},
        searchResults = emptyList(),
        createConversation = {}
    )
}