package com.saswat10.jetnetwork.presentation.accounts_center

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saswat10.jetnetwork.ui.LoginScreen
import com.saswat10.jetnetwork.ui.ProvideJNTopAppBarTitle
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.utils.formattedTime

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AccountsScreen(
    modifier: Modifier,
    openScreen: (Any) -> Unit,
    clearAndNavigate: (Any)->Unit,
    viewModel: AccountsViewModel = hiltViewModel()
) {

    val user by viewModel.user.collectAsStateWithLifecycle()

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

            Card(modifier = Modifier.card()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    if (!user.anonymous) {
                        Text(
                            text = String.format(user.email),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        )
                    }

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
                ExitAppCard { viewModel.onSignOutClick(clearAndNavigate) }
//                RemoveAccountCard { viewModel.onDeleteAccount() }
                Text(
                    "Last Sign in: " + formattedTime(user.lastSignIn) + " | " + "Joined " + formattedTime(
                        user.joined
                    ), color = Color.Gray, style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
