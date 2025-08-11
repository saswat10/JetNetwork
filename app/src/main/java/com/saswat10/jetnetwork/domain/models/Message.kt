package com.saswat10.jetnetwork.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Message(
    @DocumentId val id: String = "",
    val senderId: String = "",
    val senderName: String = "",
    val messageType: String = "",
    val content: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
