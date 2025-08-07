package com.saswat10.jetnetwork.domain.repository

import com.saswat10.jetnetwork.domain.domain_models.PostWithLikes
import com.saswat10.jetnetwork.domain.models.Post
import kotlinx.coroutines.flow.Flow

interface FeedRepository {
    val feed: Flow<List<Post>>
    val feedItems: Flow<List<PostWithLikes>>
    suspend fun toggleLike(postId: String)
//    suspend fun addComment(postId: String, comment: Comment)
}