package com.saswat10.jetnetwork.presentation.feed

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.domain_models.PostWithLikes
import com.saswat10.jetnetwork.domain.models.Comment
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val feedRepository: FeedRepository
) : JNViewModel() {

    val post: StateFlow<List<PostWithLikes>> = feedRepository.feed.flatMapLatest { posts ->
        val likeStatusFlow = posts.map { post ->
            feedRepository.getLikeStatus(post.id)
                .map { isLiked ->
                    post.id to isLiked
                }
        }

        if (likeStatusFlow.isEmpty()) {
            flowOf(emptyList())
        } else {
            combine(likeStatusFlow) { likeStatusArray ->
                val likeStatusMap = likeStatusArray.toMap()
                posts.map { post ->
                    val isLiked = likeStatusMap[post.id] ?: false
                    PostWithLikes(post, isLiked)
                }
            }
        }
    }.stateIn(
        viewModelScope, started = SharingStarted.WhileSubscribed(10_000),
        initialValue = emptyList()
    )

    val currentUserId = authRepository.currentUserId
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _comment = MutableStateFlow(Comment())
    val comment = _comment.asStateFlow()


    private val _showModalSheet = MutableStateFlow<Boolean>(false)
    val showModalSheet = _showModalSheet.asStateFlow()

    private val _showEditDialog = MutableStateFlow<Boolean>(false)
    val showDialog = _showEditDialog.asStateFlow()

    private val _selectedPostId = MutableStateFlow<String>("")

    /*
     TODO: Optimistic UI update required, update UI first, then the database
        if the database throws error, revert back to original UI
    */
    fun toggleLike(postId: String) {
        launchCatching {
            feedRepository.toggleLike(postId)
        }
    }

    fun showModalSheet() {
        _showModalSheet.value = true
    }

    fun onDismiss() {
        _showModalSheet.value = false
    }

    fun showEditDialog() {
        _showEditDialog.value = true
    }

    fun onDismissDialog() {
        _showEditDialog.value = false
    }


    fun getComments(postId: String) {
        println("Getting comments...")
        launchCatching {
            _selectedPostId.value = postId
            feedRepository.getComments(postId).collect { comments ->
                _comments.update { comments }
            }
        }
    }

    fun addComment(comment: String) {
        val commentWithPostId = Comment(postId = _selectedPostId.value, comment = comment)
        launchCatching {
            feedRepository.createComment(commentWithPostId)
        }
    }

    fun getComment(commentId: String) {
        launchCatching {
            val comment = feedRepository.getComment(_selectedPostId.value, commentId)
            _comment.update { comment ?: Comment() }
        }
    }

    fun deleteComment(commentId: String) {
        launchCatching {
            feedRepository.deleteComment(commentId, _selectedPostId.value)
        }
    }

    fun updateComment(newComment: String) {
        _comment.update {
            it.copy(comment = newComment)
        }
    }

    fun updateCommentClick() {
        launchCatching {
            feedRepository.updateComment(_comment.value)
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