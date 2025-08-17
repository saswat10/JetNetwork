package com.saswat10.jetnetwork.domain.models

data class OnlineStatus(
    val isOnline: Boolean = false,
    val lastSeen: Long? = null
)