package com.dailyease.app.data.model

import androidx.compose.ui.graphics.Color

data class Habit(
    val id: Long,
    val name: String,
    val dailyTarget: Int,
    val icon: Int,
    var currentProgress: Int = 0
) {

    val isCompleted: Boolean
        get() = currentProgress >= dailyTarget

    fun getStatusText(): String {
        return when {
            currentProgress >= dailyTarget -> "Completed"
            currentProgress == 0 -> "Not Started"
            else -> "In Progress"
        }
    }

    fun getStatusColor(): Color {
        return when {
            currentProgress >= dailyTarget -> Color(0xFF4CAF50) // Green
            currentProgress == 0 -> Color(0xFF757575) // Gray
            else -> Color(0xFF2196F3) // Blue
        }
    }
}