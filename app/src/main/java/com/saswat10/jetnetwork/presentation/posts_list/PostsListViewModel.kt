package com.saswat10.jetnetwork.presentation.posts_list

import android.util.Log
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    postRepository: PostRepository
) : JNViewModel() {

    val posts = postRepository.posts

    fun initialize() {
        launchCatching {
            authRepository.currentUser.collect { user ->
                // todo
                if (user == null) Log.d("SPLASH", "TODO move to splash screen")
            }
        }
    }
}