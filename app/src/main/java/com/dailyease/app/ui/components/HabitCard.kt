package com.dailyease.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dailyease.app.data.model.Habit

@Composable
fun HabitItem(
    habit: Habit,
    onIncrement: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    SwipeToDeleteContainer(
        onDelete = onDelete
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
                .clickable { onEdit() }
        ) {
            Column(Modifier.padding(16.dp)) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    // Icon circle
                    Card(
                        shape = CircleShape,
                        colors = CardDefaults.cardColors(Color(0xFFF3F3F3)),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = habit.icon),
                            contentDescription = habit.name,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    // Title + goal
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = habit.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Goal: ${habit.dailyTarget} times",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // + Button
                    IconButton(
                        onClick = {
                            if (habit.currentProgress < habit.dailyTarget) {
                                onIncrement()
                            }
                        },
                        enabled = habit.currentProgress < habit.dailyTarget
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(
                                    if (habit.currentProgress < habit.dailyTarget)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        Color.LightGray,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Progress text
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${habit.currentProgress} / ${habit.dailyTarget}",
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = habit.getStatusText(),
                        color = habit.getStatusColor(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Progress bar with smooth animation
                LinearProgressIndicator(
                    progress = {
                        if (habit.dailyTarget > 0) {
                            habit.currentProgress.toFloat() / habit.dailyTarget
                        } else {
                            0f
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(50)),
                    color = if (habit.isCompleted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                    trackColor = Color(0xFFE0E0E0),
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
            }
        }
    }
}