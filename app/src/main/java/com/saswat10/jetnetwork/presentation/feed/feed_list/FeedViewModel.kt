package com.saswat10.jetnetwork.presentation.feed.feed_list

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.domain_models.PostWithLikes
import com.saswat10.jetnetwork.domain.models.Comment
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val post: StateFlow<List<PostWithLikes>> = combine(
        feedRepository.feed,
        authRepository.currentUser
    ) { posts, user ->
        when {
            user == null -> {
                posts.map { PostWithLikes(it, false) }
            }

            posts.isEmpty() -> {
                emptyList()
            }

            else -> {
                posts.map { PostWithLikes(it, false) }
            }
        }
    }.flatMapLatest { postsWithDefaults ->
        if (postsWithDefaults.isEmpty()) {
            flowOf(emptyList())
        } else {
            authRepository.currentUser.flatMapLatest { user ->
                if (user == null) {
                    flowOf(postsWithDefaults)
                } else {
                    val likeStatusFlows = postsWithDefaults.map { postWithLike ->
                        feedRepository.getLikeStatus(postWithLike.post.id)
                            .map { isLiked -> postWithLike.post.id to isLiked }
                            .catch {
                                Log.w("PostViewModel", "Failed to get like status for ${postWithLike.post.id}")
                                emit(postWithLike.post.id to false)
                            }
                    }

                    combine(likeStatusFlows) { likeStatusArray ->
                        val likeStatusMap = likeStatusArray.toMap()
                        postsWithDefaults.map { postWithLike ->
                            val isLiked = likeStatusMap[postWithLike.post.id] ?: false
                            PostWithLikes(postWithLike.post, isLiked)
                        }
                    }
                }
            }
        }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
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