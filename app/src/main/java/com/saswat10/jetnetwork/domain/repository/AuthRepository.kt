package com.saswat10.jetnetwork.domain.repository

import android.net.Uri
import com.saswat10.jetnetwork.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: Flow<User?>
    val currentUserId: String
    val currentUserName: String
    val currentPhotoUrl: String
    fun hasUser(): Boolean
    fun getUserProfile(): User
    suspend fun updateDisplayName(newDisplayName: String)
    suspend fun updateProfilePic(profilePic: Uri?)
    suspend fun createAnonymousAccount()
    suspend fun linkAccountWithGoogle(idToken: String)
    suspend fun linkAccountWithEmail(email: String, password: String)
    suspend fun signInWithGoogle(idToken: String)
    suspend fun signInWithEmail(email: String, password: String)
    suspend fun signOut()
    suspend fun deleteAccount()
}