package com.saswat10.jetnetwork.presentation.splash

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.ui.FeedScreen
import com.saswat10.jetnetwork.ui.LoginScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
): JNViewModel() {

    fun onAppStart(clearAndNavigate:(Any)->Unit){
        launchCatching {
            if (!authRepository.hasUser() || authRepository.getUserProfile().anonymous) {
                authRepository.createAnonymousAccount()
                clearAndNavigate(LoginScreen)
            } else {
                Log.d("USER", "User is not anonymous")
                clearAndNavigate(FeedScreen)
            }
        }
    }
}