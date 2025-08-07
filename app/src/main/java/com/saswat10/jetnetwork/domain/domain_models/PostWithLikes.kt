package com.saswat10.jetnetwork.domain.domain_models

import com.saswat10.jetnetwork.domain.models.Post

data class PostWithLikes(
    val post: Post = Post(),
    val isLiked: Boolean = false
)