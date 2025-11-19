package com.dailyease.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyease.app.R
import com.dailyease.app.data.model.Priority
import com.dailyease.app.data.model.Task
import com.dailyease.app.util.formatDueDateForDisplay

@Composable
fun TaskCard(
    task: Task,
    onTaskClick: () -> Unit = {},
    onTaskCompleted: (Long, Boolean) -> Unit
) {
    // Animations for checked state
    val cardElevation by animateDpAsState(
        targetValue = if (task.isCompleted) 1.dp else 4.dp,
        animationSpec = tween(durationMillis = 300)
    )

    val cardAlpha by animateDpAsState(
        targetValue = if (task.isCompleted) 0.7f.dp else 1f.dp,
        animationSpec = tween(durationMillis = 300)
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (task.isCompleted) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 300)
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = cardElevation
        ),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(cardAlpha.value)
            .clickable { onTaskClick() }
    ) {

        // PRIORITY BAR with animation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    brush = when (task.priority) {
                        Priority.HIGH -> Brush.horizontalGradient(
                            listOf(
                                if (task.isCompleted) Color(0x66FF5252) else Color(0xFFFF5252),
                                if (task.isCompleted) Color(0x66FF8A65) else Color(0xFFFF8A65)
                            )
                        )
                        Priority.MEDIUM -> Brush.horizontalGradient(
                            listOf(
                                if (task.isCompleted) Color(0x66FFD54F) else Color(0xFFFFD54F),
                                if (task.isCompleted) Color(0x66FFB74D) else Color(0xFFFFB74D)
                            )
                        )
                        Priority.LOW -> Brush.horizontalGradient(
                            listOf(
                                if (task.isCompleted) Color(0x6664B5F6) else Color(0xFF64B5F6),
                                if (task.isCompleted) Color(0x664DD0E1) else Color(0xFF4DD0E1)
                            )
                        )
                    }
                )
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        )

        // ------- CONTENT -------
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Custom Checkbox with better visual feedback
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (task.isCompleted) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        },
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onTaskCompleted(task.id, !task.isCompleted)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (task.isCompleted) {
                    Icon(
                        painter = painterResource(R.drawable.ic_check_circle),
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // ------- TEXT SECTION -------
            Column(modifier = Modifier.weight(1f)) {
                // Title with completion styling
                Text(
                    text = task.title,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (task.isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description (if exists)
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        fontSize = 14.sp,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (task.isCompleted) {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 2,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }

                // Due Date
                Text(
                    text = formatDueDateForDisplay(task.dueDate),
                    fontSize = 14.sp,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (task.isCompleted) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                Spacer(modifier = Modifier.height(8.dp))

                // TAGS
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PriorityTag(priority = task.priority, isCompleted = task.isCompleted)
                    RepeatingTag(repeating = task.repeating, isCompleted = task.isCompleted)
                }
            }
        }
    }
}