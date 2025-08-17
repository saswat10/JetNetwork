package com.saswat10.jetnetwork.presentation.accounts_center

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.ui.LoginScreen
import com.saswat10.jetnetwork.ui.PostScreen
import com.saswat10.jetnetwork.ui.ProvideJNTopAppBarAction
import com.saswat10.jetnetwork.ui.ProvideJNTopAppBarTitle

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountsScreen(
    modifier: Modifier,
    openScreen: (Any) -> Unit,
    clearAndNavigate: (Any) -> Unit,
    viewModel: AccountsViewModel = hiltViewModel()
) {

    val user by viewModel.user.collectAsStateWithLifecycle()
    val posts by viewModel.posts.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.getUserProfileOnRefresh() }
    Scaffold {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProvideJNTopAppBarTitle {
                Text(
                    stringResource(R.string.account_center),
                )
            }
            ProvideJNTopAppBarAction {
                if (!user.anonymous) {
                    ExitAppCard { viewModel.onSignOutClick(clearAndNavigate) }
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            if (user.anonymous) {
                AccountCenterCard(
                    stringResource(R.string.authenticate),
                    Icons.Filled.AccountCircle,
                    Modifier.card()
                ) {
                    openScreen(LoginScreen)
                }
            } else {
                if (user.photoUrl.isNullOrBlank()) {
                    Icon(Icons.Default.AccountCircle, user.displayName, Modifier.size(100.dp))
                } else {
                    AsyncImage(
                        model = user.photoUrl,
                        contentDescription = user.displayName,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
                DisplayNameCard(user.displayName) { viewModel.onUpdateDisplayNameClick(it) }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
                LazyColumn(modifier = Modifier.padding(12.dp)) {
                    item {
                        Text("Your Posts", style = MaterialTheme.typography.titleLarge)
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        )
                        HorizontalDivider()
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(6.dp)
                        )
                    }
                    posts.forEach { post ->
                        item {
                            OutlinedCard(modifier = Modifier.fillMaxWidth(), onClick = {
                                openScreen(PostScreen(post.id))
                            }) {
                                Column(modifier.padding(16.dp, 18.dp)) {
                                    Text(post.title.ifBlank { "No Title" }, style = MaterialTheme.typography.titleMedium)
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(6.dp)
                                    )
                                    Text(
                                        post.content.ifBlank { "No Content" },
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(6.dp)
                                    )
                                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                                        Card() {
                                            if(post.private){
                                                Text("Private", Modifier.padding(10.dp), style = MaterialTheme.typography.labelSmall)
                                            }else{
                                                Text("Public", Modifier.padding(10.dp), style = MaterialTheme.typography.labelSmall)
                                            }
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Card() {
                                                Text(
                                                    "${post.likes} likes",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    modifier = Modifier.padding(10.dp)
                                                )
                                            }
                                            Card() {
                                                Text(
                                                    "${post.comments} comment(s)",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    modifier = Modifier.padding(10.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp)
                            )
                        }
                    }
                }

            }
        }
    }
}
