package com.saswat10.jetnetwork.domain.models

import com.google.firebase.firestore.DocumentId

data class Conversation(
    @DocumentId val id: String = "",
    val participantIds: List<String> = emptyList(),
    val participants: List<User> = emptyList(),
    val lastMessage: Message = Message(),
)
