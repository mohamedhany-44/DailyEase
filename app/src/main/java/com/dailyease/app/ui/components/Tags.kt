// Tags.kt
package com.dailyease.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyease.app.data.model.Priority
import com.dailyease.app.data.model.RepeatType

@Composable
fun PriorityTag(priority: Priority, isCompleted: Boolean = false) {
    val bgColor by animateColorAsState(
        targetValue = when (priority) {
            Priority.HIGH -> if (isCompleted) Color(0x33C62828) else Color(0xFFFFEBEE)
            Priority.MEDIUM -> if (isCompleted) Color(0x33F57C00) else Color(0xFFFFF8E1)
            Priority.LOW -> if (isCompleted) Color(0x330277BD) else Color(0xFFE3F2FD)
        }
    )

    val textColor by animateColorAsState(
        targetValue = when (priority) {
            Priority.HIGH -> if (isCompleted) Color(0x99C62828) else Color(0xFFC62828)
            Priority.MEDIUM -> if (isCompleted) Color(0x99F57C00) else Color(0xFFF57C00)
            Priority.LOW -> if (isCompleted) Color(0x990277BD) else Color(0xFF0277BD)
        }
    )

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = priority.name.lowercase().replaceFirstChar { it.uppercase() },
            fontSize = 12.sp,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}

@Composable
fun RepeatingTag(repeating: RepeatType, isCompleted: Boolean = false) {
    val bgColor by animateColorAsState(
        targetValue = if (isCompleted) {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }
    )

    val textColor by animateColorAsState(
        targetValue = if (isCompleted) {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    )

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = when (repeating) {
                RepeatType.NONE -> "One-time"
                RepeatType.DAILY -> "Daily"
                RepeatType.WEEKLY -> "Weekly"
            },
            fontSize = 12.sp,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}