package com.saswat10.jetnetwork.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class Comment(
    @DocumentId val id: String="",
    val comment: String = "",
    val postId: String = "",
    val userId: String = "",
    val username: String = "",
    val photoUrl: String = "",
    @ServerTimestamp val createdAt: Timestamp= Timestamp.now(),
)