package com.saswat10.jetnetwork.utils

import com.google.firebase.Timestamp
import java.util.concurrent.TimeUnit

private const val SECOND = 1
private const val MINUTE = 60
private const val HOUR = 3600
private const val DAY = 86400
private const val WEEK = 604800
private const val MONTH = 2629743
private const val YEAR = 31556926

fun formattedTime(timestamp: Timestamp): String {
    val now = Timestamp.now().seconds
    val seconds = (now - timestamp.seconds)

    return when {
        seconds < MINUTE -> "Just now"
        seconds < 2 * MINUTE -> "a minute ago"
        seconds < 50 * MINUTE -> "${seconds / MINUTE} minutes ago"
        seconds < 90 * MINUTE -> "an hour ago"
        seconds < 24 * HOUR -> "${seconds / HOUR} hours ago"
        seconds < 48 * HOUR -> "yesterday"
        seconds < 30 * DAY -> "${seconds / DAY} days ago"
        seconds < 12 * MONTH -> "${seconds / MONTH} months ago"
        seconds < YEAR -> "${seconds / YEAR} years ago"
        else -> "a long time ago"
    }
}

fun formattedTime(timeInMillis: Long): String {
    val now = Timestamp.now().seconds
    val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis)
    val seconds = (now - timeInSeconds)

    return when {
        seconds < MINUTE -> "Just now"
        seconds < 2 * MINUTE -> "a minute ago"
        seconds < 50 * MINUTE -> "${seconds / MINUTE} minutes ago"
        seconds < 90 * MINUTE -> "an hour ago"
        seconds < 24 * HOUR -> "${seconds / HOUR} hours ago"
        seconds < 48 * HOUR -> "yesterday"
        seconds < 30 * DAY -> "${seconds / DAY} days ago"
        seconds < 12 * MONTH -> "${seconds / MONTH} months ago"
        seconds < YEAR -> "${seconds / YEAR} years ago"
        else -> "a long time ago"
    }
}
