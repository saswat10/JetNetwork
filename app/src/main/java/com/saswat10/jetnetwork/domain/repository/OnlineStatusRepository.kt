package com.saswat10.jetnetwork.domain.repository

import com.saswat10.jetnetwork.domain.models.OnlineStatus
import kotlinx.coroutines.flow.Flow

interface OnlineStatusRepository {
    
    fun setOnlineStatus(userId: String)
    fun getOnlineStatus(userId: String): Flow<OnlineStatus>
}