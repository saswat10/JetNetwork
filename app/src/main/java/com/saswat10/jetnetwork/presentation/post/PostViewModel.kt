package com.saswat10.jetnetwork.presentation.post

import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.models.Post
import com.saswat10.jetnetwork.domain.models.User
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.PostRepository
import com.saswat10.jetnetwork.utils.DEFAULT_POST_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository
): JNViewModel() {

    private val _post = MutableStateFlow(Post(DEFAULT_POST_ID))
    val post: StateFlow<Post> = _post.asStateFlow()

    private val _user = MutableStateFlow<User>(User())
    val user = _user.asStateFlow()

    fun initialize(postId: String){
        launchCatching {
            _post.value = postRepository.readPost(postId) ?: Post(DEFAULT_POST_ID)
        }
    }

    init {
        observerAuthenticationState()
    }

    private fun observerAuthenticationState(){
        launchCatching {
            authRepository.currentUser.collect { user ->
                if (user != null) {
                    _user.value = user
                }
            }
        }
    }

    fun updateTitle(newTitle: String){
        _post.value = _post.value.copy(title = newTitle)
    }

    fun updateContent(newContent: String){
        _post.value = _post.value.copy(content = newContent)
    }

    fun toggleVisibility(){
        _post.update {
            it.copy(
                private =  !it.private
            )
        }
    }

    fun savePost(popUpScreen: ()->Unit){
        launchCatching {
            if(_post.value.id == DEFAULT_POST_ID){
                postRepository.createPost(_post.value)
                _post.value = Post(DEFAULT_POST_ID)
            }else{
                postRepository.updatePost(_post.value)
                _post.value = Post(DEFAULT_POST_ID)
            }
        }
        popUpScreen()
    }

}
