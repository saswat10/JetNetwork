package com.saswat10.jetnetwork.data

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.saswat10.jetnetwork.domain.models.OnlineStatus
import com.saswat10.jetnetwork.domain.repository.OnlineStatusRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class OnlineStatusRepositoryImpl @Inject constructor() : OnlineStatusRepository {
    override fun getOnlineStatus(userId: String): Flow<OnlineStatus> = callbackFlow {
        val userStatusRef = Firebase.database.getReference("users").child(userId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Check if the data exists before trying to read it
                if (snapshot.exists()) {
                    val isOnline = snapshot.child("isOnline").getValue(Boolean::class.java) ?: false
                    val lastSeen = snapshot.child("lastSeen").getValue(Long::class.java)

                    // Send the new status to the Flow
                    trySend(OnlineStatus(isOnline, lastSeen))
                } else {
                    // If the node doesn't exist, assume they are offline
                    trySend(OnlineStatus())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        userStatusRef.addValueEventListener(listener)

        // The awaitClose block is called when the flow collector is cancelled
        awaitClose {
            userStatusRef.removeEventListener(listener)
        }
    }

    override fun setOnlineStatus(userId: String) {
        val userStatusRef = Firebase.database.getReference("users").child(userId)
        userStatusRef.onDisconnect().setValue(
            mapOf(
                "isOnline" to false,
                "lastSeen" to ServerValue.TIMESTAMP
            )
        )
        userStatusRef.setValue(
            mapOf(
                "isOnline" to true,
                "lastSeen" to ServerValue.TIMESTAMP
            )
        )
    }
}
