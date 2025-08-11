package com.saswat10.jetnetwork.presentation.chat.chat_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.saswat10.jetnetwork.presentation.chat.components.SimpleSearchBar

@Composable
fun ChatListScreen(){

    Column(modifier = Modifier.fillMaxSize()){
        SimpleSearchBar(
            textFieldState = TextFieldState(""),
            onSearch = {},
            searchResults = emptyList(),
            modifier = Modifier
        )
        LazyColumn {
            items(10){
                ListItem(
                    headlineContent = {Text("$it number")}
                )
            }
        }
    }
}