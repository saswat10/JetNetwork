package com.saswat10.jetnetwork.domain.repository

import com.saswat10.jetnetwork.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    val posts: Flow<List<Post>>
    suspend fun createPost(post: Post)
    suspend fun readPost(postId: String): Post?
    suspend fun updatePost(post: Post)
    suspend fun deletePost(postId: String)
}