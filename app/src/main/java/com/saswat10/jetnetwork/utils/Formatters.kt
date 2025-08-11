package com.saswat10.jetnetwork.utils

import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

private const val SECOND = 1
private const val MINUTE = 60
private const val HOUR = 3600
private const val DAY = 86400
private const val WEEK = 604800
private const val MONTH = 2678400
private const val YEAR = 31536000

fun formattedTime(timestamp: Timestamp): String {
    val now = Timestamp.now().seconds
    val seconds = (now - timestamp.seconds)

    return parseDateTime(seconds)
}

fun formattedTime(timeInMillis: Long): String {
    val now = Timestamp.now().seconds
    val timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis)
    val seconds = (now - timeInSeconds)

    return parseDateTime(seconds)
}


private fun parseDateTime(seconds: Long): String{
    return when {
        seconds > YEAR -> "${seconds/YEAR}y ago"
        seconds > MONTH -> "${seconds/MONTH}m ago"
        seconds > WEEK -> "${seconds/WEEK}w ago"
        seconds > DAY -> "${seconds/DAY}d ago"
        seconds > HOUR -> "${seconds/HOUR}hr ago"
        seconds > MINUTE -> "${seconds/MINUTE}min ago"
        seconds > SECOND -> "${seconds/SECOND}s ago"
        else -> "Just Now"
    }
}

fun localTimeString(timestamp: Timestamp): String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return timestamp.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(formatter)
}

fun formatChatDate(timestamp: Timestamp): String {
    val date = timestamp.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    return when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
}

fun formatName(string: String): String{
    return string.ifBlank { "anonymous user" }
}