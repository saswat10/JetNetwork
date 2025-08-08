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
    val currentUserId = authRepository.currentUserId
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    private val _comment = MutableStateFlow<Comment>(Comment())
    val comment = _comment.asStateFlow()


    private val _showModalSheet = MutableStateFlow<Boolean>(false)
    val showModalSheet = _showModalSheet.asStateFlow()

    private  val _showEditDialog = MutableStateFlow<Boolean>(false)
    val showDialog = _showEditDialog.asStateFlow()

    private val _selectedPostId = MutableStateFlow<String>("")

    /*
     TODO: Optimistic UI update required, update UI first, then the database
        if the database throws error, revert back to original UI
    */
    fun toggleLike(postId: String){
        launchCatching {
            feedRepository.toggleLike(postId)
        }
    }

    fun showModalSheet(){
        _showModalSheet.value = true
    }

    fun onDismiss(){
        _showModalSheet.value = false
    }

    fun showEditDialog(){
        _showEditDialog.value = true
    }

    fun onDismissDialog(){
        _showEditDialog.value = false
    }


    fun getComments(postId: String){
        println("Getting comments...")
        launchCatching {
            _selectedPostId.value = postId
            feedRepository.getComments(postId).collect {comments ->
                _comments.update { comments }
            }
        }
    }

    fun addComment(comment: String){
        val commentWithPostId = Comment(postId = _selectedPostId.value, comment = comment)
        launchCatching {
            feedRepository.createComment(commentWithPostId)
        }
    }

    fun getComment(commentId: String){
        launchCatching {
            val comment = feedRepository.getComment(_selectedPostId.value, commentId)
            _comment.update { comment?: Comment() }
        }
    }

    fun deleteComment(commentId: String){
        launchCatching {
            feedRepository.deleteComment(commentId, _selectedPostId.value)
        }
    }

    fun updateComment(newComment: String){
        _comment.update {
            it.copy(comment = newComment)
        }
    }

    fun updateCommentClick(){
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