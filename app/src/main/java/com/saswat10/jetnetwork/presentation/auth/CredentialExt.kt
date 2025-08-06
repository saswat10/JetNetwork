package com.saswat10.jetnetwork.presentation.auth

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.saswat10.jetnetwork.R
import kotlinx.coroutines.launch

@Composable
fun AuthenticationButton(buttonText: Int, onRequestResult: (Credential) -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                launchCredentialManagerButtonUI(context, onRequestResult)
            }
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 10.dp)
    ) {
        Icon(painter = painterResource(R.drawable.ic_google), contentDescription = "Google Logo")
        Spacer(modifier = Modifier.padding(4.dp))
        Text(text = stringResource(buttonText))
    }
}


private suspend fun launchCredentialManagerButtonUI(
    context: Context,
    onRequestResult: (Credential) -> Unit
){
    try {
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = context.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest
            .Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = CredentialManager
            .create(context)
            .getCredential(
                request = request,
                context = context
            )

        onRequestResult(result.credential)
    }catch (e: NoCredentialException){
        // add code TODO
    }catch (e: GetCredentialException){
        // add code TODO
    }
}

suspend fun launchCredentialManagerBottomSheet(
    context: Context,
    hasFilter: Boolean = true,
    onRequestResult: (Credential) -> Unit
){

    try {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(hasFilter)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest
            .Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = CredentialManager
            .create(context)
            .getCredential(
                context = context,
                request = request
            )

        onRequestResult(result.credential)
    }catch (e: NoCredentialException){

        // add code TODO
        //If the bottom sheet was launched with filter by authorized accounts, we launch it again
        //without filter so the user can see all available accounts, not only the ones that have
        //been previously authorized in this app
        if (hasFilter) {
            launchCredentialManagerBottomSheet(context, hasFilter = false, onRequestResult)
        }
    }catch (e: GetCredentialException){
        // add code TODO
    }

}
