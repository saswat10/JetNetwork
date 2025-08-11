package com.saswat10.jetnetwork.domain.models

import com.google.firebase.firestore.DocumentId

data class User(
    @DocumentId val id: String = "",
    val email: String = "",
    val provider: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val isAnonymous: Boolean = true,
    val joined: Long = 0L, // in millis
    val lastSignIn: Long = 0L // in millis
)