package com.saswat10.jetnetwork.presentation.feed

import android.util.Log
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.models.Comment
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.FeedRepository
import com.saswat10.jetnetwork.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    postRepository: PostRepository,
    private val feedRepository: FeedRepository
) : JNViewModel() {

    val postWithLikes = feedRepository.feedItems
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    /*
     TODO: Optimistic UI update required, update UI first, then the database
        if the database throws error, revert back to original UI
    */
    fun toggleLike(postId: String){
        launchCatching {
            feedRepository.toggleLike(postId)
        }
    }

    fun getComments(postId: String){
        println("Getting comments...")
        launchCatching {
            feedRepository.getComments(postId).collect {comments ->
                _comments.update { comments }
            }
        }
    }

    fun addComment(postId:String, comment: String){
        val commentWithPostId = Comment(postId = postId, comment = comment)
        launchCatching {
            feedRepository.createComment(commentWithPostId)
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