package com.saswat10.jetnetwork.presentation.accounts_center

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.models.User
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.PostRepository
import com.saswat10.jetnetwork.ui.SplashScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AccountsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    postRepository: PostRepository
) : JNViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user.asStateFlow()

    val posts = postRepository.posts.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(2000),
        emptyList()
    )

    init {
        launchCatching {
            _user.value = authRepository.getUserProfile()
        }
    }

    fun getUserProfileOnRefresh(){
        launchCatching {
                _user.value = authRepository.getUserProfile()
        }
    }

    fun onUpdateDisplayNameClick(newDisplayName: String){
        launchCatching {
            authRepository.updateDisplayName(newDisplayName)
            _user.value = authRepository.getUserProfile()
            println(_user.value)
        }
    }

    fun onUpdateProfilePicClick(newProfilePic: Uri?){
        launchCatching {
            println(newProfilePic)
            authRepository.updateProfilePic(newProfilePic)
            _user.value = authRepository.getUserProfile()
            println(_user.value)
        }
    }


    fun onSignOutClick(clearAndNavigate:(Any)-> Unit){
        launchCatching {
            authRepository.signOut()
            clearAndNavigate(SplashScreen)
            // TODO: add logic for restarting the app
        }
    }

    fun onDeleteAccount(){
        launchCatching {
            authRepository.deleteAccount()
            // TODO: add logic for restarting the app
            // TODO: Reauthentication required
        }
    }

}