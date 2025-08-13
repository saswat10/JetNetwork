package com.saswat10.jetnetwork.domain.repository

import com.saswat10.jetnetwork.domain.domain_models.PostWithLikes
import com.saswat10.jetnetwork.domain.models.Comment
import com.saswat10.jetnetwork.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    val feed: Flow<List<Post>>
    val feedItems: Flow<List<PostWithLikes>>
    suspend fun toggleLike(postId: String)
    fun getLikeStatus(postId: String): Flow<Boolean>
    suspend fun getComments(postId: String): Flow<List<Comment>>
    suspend fun getComment(postId: String, commentId: String): Comment?
    suspend fun createComment(comment: Comment)
    suspend fun updateComment(comment: Comment)
    suspend fun deleteComment(commentId: String, postId: String)
}