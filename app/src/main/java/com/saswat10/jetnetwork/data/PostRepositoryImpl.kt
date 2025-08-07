package com.saswat10.jetnetwork.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.saswat10.jetnetwork.domain.models.Post
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository
): PostRepository {
    override val posts: Flow<List<Post>>
        get() = authRepository.currentUser.flatMapLatest {user ->
            Firebase.firestore
                .collection(POSTS_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, user?.id)
                .dataObjects()
        }


    override suspend fun createPost(post: Post) {
        val postWithUserData = post.copy(userId = authRepository.currentUserId, username = authRepository.currentUserName, photoUrl = authRepository.currentPhotoUrl)
        Firebase.firestore
            .collection(POSTS_COLLECTION)
            .add(postWithUserData).await()
    }

    override suspend fun readPost(postId: String): Post? {
        return Firebase.firestore
            .collection(POSTS_COLLECTION)
            .document(postId).get().await().toObject()
    }

    override suspend fun updatePost(post: Post) {
        Firebase.firestore
            .collection(POSTS_COLLECTION)
            .document(post.id).set(post).await()
    }

    override suspend fun deletePost(postId: String) {
        Firebase.firestore
            .collection(POSTS_COLLECTION)
            .document(postId).delete().await()
    }

    companion object{
        private const val USER_ID_FIELD = "userId"
        private const val POSTS_COLLECTION = "posts"
    }
}