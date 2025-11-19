package com.dailyease.app.util

import com.dailyease.app.data.model.RepeatType
import java.text.SimpleDateFormat
import java.util.*

fun formatDueDateForDisplay(dueDate: Long): String {
    val date = Date(dueDate)
    val formatter = SimpleDateFormat("EEE, MMM d, h:mm a", Locale.getDefault())
    return formatter.format(date)
}

fun getRepeatingDisplayText(repeating: RepeatType): String {
    return when (repeating) {
        RepeatType.NONE -> "Does not repeat"
        RepeatType.DAILY -> "Repeats daily"
        RepeatType.WEEKLY -> "Repeats weekly"
    }
}