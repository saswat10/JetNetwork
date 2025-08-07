package com.saswat10.jetnetwork.presentation.posts_list

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.saswat10.jetnetwork.JNViewModel
import com.saswat10.jetnetwork.domain.models.Post
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class PostsListViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val postRepository: PostRepository
): JNViewModel(){

    val posts = postRepository.posts
    val postFlow = postRepository.posts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )
    private val _title =  MutableStateFlow<String>("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _content = MutableStateFlow<String>("")
    val content: StateFlow<String> = _content.asStateFlow()

    fun updateTitle(newTitle: String){
        _title.value = newTitle
    }

    fun updateContent(newContent: String){
        _content.value = newContent
    }

    fun createNewPost(){
        launchCatching {
            val post = Post(
                title = _title.value,
                content = _content.value,
                private = true
            )
            postRepository.createPost(post)
        }
    }



    fun initialize(){
        launchCatching {
            authRepository.currentUser.collect { user ->
                // todo
                if(user==null) Log.d("SPLASH", "TODO move to splash screen")
            }
        }
    }
}