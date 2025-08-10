package com.saswat10.jetnetwork.presentation.auth.login

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.saswat10.jetnetwork.FeedScreen
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.LoginScreen
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
): JNViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun updateEmail(newEmail: String){
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String){
        _password.value = newPassword
    }

    fun handleSignIn(openAndPopUp: (Any, Any) -> Unit) {
        launchCatching {
            authRepository.signInWithEmail(_email.value, _password.value)
            openAndPopUp(FeedScreen, LoginScreen)
        }
    }
    fun onSignInWithGoogle(credential: Credential, openAndPopUp: (Any, Any) -> Unit){
        launchCatching {
            if(credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                authRepository.signInWithGoogle(googleIdTokenCredential.idToken)
                openAndPopUp(FeedScreen, LoginScreen)
            }else{
                Log.d("ERROR", "Unexpected Credential")
            }
        }
    }

}