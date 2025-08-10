package com.saswat10.jetnetwork

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.saswat10.jetnetwork.domain.models.User
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
): JNViewModel() {

    fun onAppStart(){
        if(authRepository.hasUser()) Log.d("USER", "user present")
        else createAnonymously()
    }

    private fun createAnonymously(){
        viewModelScope.launch{
            authRepository.createAnonymousAccount()
        }
    }


}