package com.saswat10.jetnetwork.domain.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp


data class Post(
    @DocumentId val id: String = "",
    val title: String = "",
    val content: String = "",
    val private: Boolean = true,
    val userId: String = "",
    val username: String = "",
    val photoUrl: String = "",
    @ServerTimestamp val createdAt: Timestamp = Timestamp.now()
){
    @Exclude
    fun getTitleString(): String{
        val isLongText = this.title.length > TITLE_MAX_SIZE
        val endRange = if(isLongText) TITLE_MAX_SIZE else this.title.length-1;
        return this.title.substring(IntRange(0, endRange))
    }

    companion object{
        const val TITLE_MAX_SIZE = 30
    }
}