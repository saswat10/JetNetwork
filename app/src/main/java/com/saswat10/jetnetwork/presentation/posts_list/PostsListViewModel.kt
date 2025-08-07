package com.saswat10.jetnetwork.presentation.posts_list

import android.util.Log
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.FeedRepository
import com.saswat10.jetnetwork.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    postRepository: PostRepository,
    private val feedRepository: FeedRepository
) : JNViewModel() {

    val postWithLikes = feedRepository.feedItems

    /*
     TODO: Optimistic UI update required, update UI first, then the database
        if the database throws error, revert back to original UI
    */
    fun toggleLike(postId: String){
        launchCatching {
            feedRepository.toggleLike(postId)
        }
    }
    fun initialize() {
        launchCatching {
            authRepository.currentUser.collect { user ->
                // todo
                if (user == null) Log.d("SPLASH", "TODO move to splash screen")
            }
        }
    }
}