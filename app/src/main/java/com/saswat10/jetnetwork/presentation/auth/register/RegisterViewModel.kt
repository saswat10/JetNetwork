package com.saswat10.jetnetwork.presentation.auth.register

import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.saswat10.jetnetwork.ui.FeedScreen
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.presentation.auth.isValidEmail
import com.saswat10.jetnetwork.presentation.auth.isValidPassword
import com.saswat10.jetnetwork.utils.INVALID_EMAIL
import com.saswat10.jetnetwork.utils.INVALID_PASSWORD
import com.saswat10.jetnetwork.utils.PASSWORDS_DO_NOT_MATCH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
): JNViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    fun updateEmail(newEmail: String){
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String){
        _password.value = newPassword
    }

    fun updateConfirmPassword(newConfirmPassword: String){
        _confirmPassword.value = newConfirmPassword
    }

    fun handleRegister(clearAndNavigate:(Any)-> Unit){
        launchCatching {
            if(!_email.value.isValidEmail()){
                throw IllegalArgumentException(INVALID_EMAIL)
            }

            if(!_password.value.isValidPassword()){
                throw IllegalArgumentException(INVALID_PASSWORD)
            }

            if(_password.value != _confirmPassword.value){
                throw IllegalArgumentException(PASSWORDS_DO_NOT_MATCH)
            }

            authRepository.linkAccountWithEmail(_email.value, _password.value)
            clearAndNavigate(FeedScreen)
        }
    }
    fun onSignInWithGoogle(credential: Credential, clearAndNavigate:(Any)-> Unit){
        launchCatching {
            if(credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                authRepository.linkAccountWithGoogle(googleIdTokenCredential.idToken)
                clearAndNavigate(FeedScreen)
            }else{
                Log.d("ERROR", "Unexpected Credential")
            }
        }
    }
}