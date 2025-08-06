package com.saswat10.jetnetwork.presentation.auth.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.saswat10.jetnetwork.R
import com.saswat10.jetnetwork.presentation.auth.AuthenticationButton
import com.saswat10.jetnetwork.presentation.auth.launchCredentialManagerBottomSheet

@Composable
fun LoginScreen(modifier: Modifier, viewModel: LoginViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        launchCredentialManagerBottomSheet(context = context) { credential ->
            viewModel.onSignInWithGoogle(credential)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        ElevatedCard() {
            Text(
                text = stringResource(R.string.sign_in_button).uppercase(),
                modifier = Modifier.fillMaxWidth().padding(16.dp, 10.dp),
                style = MaterialTheme.typography.displaySmall)

            HorizontalDivider()

            OutlinedTextField(
                value = email.value,
                onValueChange = { viewModel.updateEmail(it) },
                modifier = Modifier.fillMaxWidth().padding(16.dp, 10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "email") },
                placeholder = { Text(stringResource(R.string.email)) } )

            OutlinedTextField(
                value = password.value,
                onValueChange = { viewModel.updatePassword(it) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().padding(16.dp, 10.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "password") },
                placeholder = { Text(stringResource(R.string.password)) } )

            Button(
                onClick = {viewModel.handleSignIn()},
                modifier = Modifier.fillMaxWidth().padding(16.dp, 10.dp),
            ) { Text(text=stringResource(R.string.sign_in_button)) }

            Text(
                text = stringResource(R.string.or),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge)

            AuthenticationButton(buttonText = R.string.sign_in_google_button) { credential ->
                viewModel.onSignInWithGoogle(credential)
            }
        }
    }
}
