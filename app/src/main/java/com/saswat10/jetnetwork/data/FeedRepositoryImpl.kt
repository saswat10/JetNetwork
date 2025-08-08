package com.saswat10.jetnetwork.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.saswat10.jetnetwork.domain.domain_models.PostWithLikes
import com.saswat10.jetnetwork.domain.models.Comment
import com.saswat10.jetnetwork.domain.models.Post
import com.saswat10.jetnetwork.domain.repository.AuthRepository
import com.saswat10.jetnetwork.domain.repository.FeedRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class FeedRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository
) : FeedRepository {
    override val feed: Flow<List<Post>>
        get() = authRepository.currentUser.flatMapLatest { user ->
            Firebase.firestore
                .collection(POSTS_COLLECTION)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .dataObjects<Post>()
        }

    override val feedItems: Flow<List<PostWithLikes>>
        get() = authRepository.currentUser.flatMapLatest { user ->
            Firebase.firestore
                .collection(POSTS_COLLECTION)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .dataObjects<Post>()
                .mapLatest { posts ->
                    posts.map {
                        val isLiked = checkLikeStatus(it.id, authRepository.currentUserId)
                        PostWithLikes(post = it, isLiked = isLiked)
                    }
                }
        }

    override suspend fun toggleLike(postId: String) {

        Firebase.firestore.runTransaction {
            val userLike = it.get(
                Firebase.firestore.collection(POSTS_COLLECTION).document(postId)
                    .collection(LIKES_SUBCOLLECTION).document(authRepository.currentUserId)
            ).exists()

            if (userLike) {
                it.delete(
                    Firebase.firestore.collection(POSTS_COLLECTION).document(postId)
                        .collection(LIKES_SUBCOLLECTION).document(authRepository.currentUserId)
                )
                it.update(
                    Firebase.firestore.collection(POSTS_COLLECTION).document(postId), FIELD_LIKES,
                    FieldValue.increment(-1)
                )
            } else {

                it.set(
                    Firebase.firestore.collection(POSTS_COLLECTION).document(postId)
                        .collection(LIKES_SUBCOLLECTION).document(authRepository.currentUserId),
                    mapOf<String, Any>()
                )

                it.update(
                    Firebase.firestore.collection(POSTS_COLLECTION).document(postId), FIELD_LIKES,
                    FieldValue.increment(+1)
                )
            }
        }.await()
    }

    override suspend fun getComments(postId: String): Flow<List<Comment>> {
        return authRepository.currentUser.flatMapLatest { user ->
            Firebase.firestore
                .collection(POSTS_COLLECTION)
                .document(postId)
                .collection(COMMENTS_SUBCOLLECTION)
                .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                .dataObjects<Comment>()
        }
    }

    override suspend fun getComment(
        postId: String,
        commentId: String
    ): Comment? {
        return Firebase.firestore
            .collection(POSTS_COLLECTION)
            .document(postId)
            .collection(COMMENTS_SUBCOLLECTION)
            .document(commentId)
            .get()
            .await()
            .toObject()
    }

    override suspend fun createComment(comment: Comment) {
        val commentWithUserData = comment.copy(
            userId = authRepository.currentUserId,
            username = authRepository.currentUserName,
            photoUrl = authRepository.currentPhotoUrl
        )
        Firebase.firestore.runTransaction {
            val commentRef = Firebase.firestore
                .collection(POSTS_COLLECTION)
                .document(comment.postId)
                .collection(COMMENTS_SUBCOLLECTION).document()
            it.set(commentRef, commentWithUserData)
            it.update(Firebase.firestore
                .collection(POSTS_COLLECTION)
                .document(comment.postId), FIELD_COMMENTS, FieldValue.increment((+1)))
        }.await()
    }

    override suspend fun updateComment(comment: Comment) {
        Firebase.firestore
            .collection(POSTS_COLLECTION)
            .document(comment.postId)
            .collection(COMMENTS_SUBCOLLECTION)
            .document(comment.id)
            .set(comment).await()
    }

    override suspend fun deleteComment(commentId: String, postId: String) {
        Firebase.firestore.runTransaction {
            it.delete(Firebase.firestore
                .collection(POSTS_COLLECTION)
                .document(postId)
                .collection(COMMENTS_SUBCOLLECTION)
                .document(commentId))
            it.update(Firebase.firestore
                .collection(POSTS_COLLECTION)
                .document(postId), FIELD_COMMENTS, FieldValue.increment((-1)))
        }.await()
    }

    private suspend fun checkLikeStatus(postId: String, userId: String): Boolean {
        return try {
            Firebase.firestore
                .collection(POSTS_COLLECTION).document(postId)
                .collection(LIKES_SUBCOLLECTION).document(userId)
                .get().await().exists()
        } catch (e: Exception) {
            // Handle potential errors, e.g., log them and return false
            false
        }
    }

    companion object {
        private const val POSTS_COLLECTION = "posts"
        private const val LIKES_SUBCOLLECTION = "likes"
        private const val COMMENTS_SUBCOLLECTION = "comments"
        private const val FIELD_LIKES = "likes"
        private const val FIELD_COMMENTS = "comments"
        private const val TIMESTAMP = "createdAt"
    }
}