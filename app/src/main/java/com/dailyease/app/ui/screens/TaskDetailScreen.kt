package com.dailyease.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyease.app.R
import com.dailyease.app.data.model.Priority
import com.dailyease.app.data.model.RepeatType
import com.dailyease.app.data.model.Task
import com.dailyease.app.ui.components.MetadataItem
import com.dailyease.app.util.formatDueDateForDisplay
import com.dailyease.app.util.getRepeatingDisplayText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    task: Task,
    onClose: () -> Unit = {},
    onMarkComplete: () -> Unit = {},
    onDelete: () -> Unit = {},
    onEdit: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {

        //   TOP GRADIENT HEADER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(
                    Brush.verticalGradient(
                        colors = if (task.isCompleted) {
                            listOf(
                                Color(0xFF81C784), // Light green
                                Color(0xFF4CAF50)  // Green
                            )
                        } else {
                            listOf(
                                Color(0xFFB388FF), // Light purple
                                Color(0xFF82B1FF)  // Light blue
                            )
                        }
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    "DailyEase",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.9f)
                )
                Text(
                    if (task.isCompleted) "Task Completed! 🎉" else "Your daily tasks, simplified.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // -----------------------------
        //   BOTTOM SHEET
        // -----------------------------
        ModalBottomSheet(
            onDismissRequest = onClose,
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(42.dp)
                            .height(5.dp)
                            .background(
                                color = if (task.isCompleted) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                },
                                shape = RoundedCornerShape(3.dp)
                            )
                    )
                }
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp)
            ) {

                // -----------------------------
                // HEADER ROW - Improved layout
                // -----------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    // Left side: Icon and Title
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            painter = painterResource(
                                if (task.isCompleted) R.drawable.ic_check_circle
                                else R.drawable.ic_task
                            ),
                            contentDescription = "Task",
                            modifier = Modifier.size(52.dp),
                            tint = if (task.isCompleted) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = if (task.isCompleted) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            },
                            textDecoration = if (task.isCompleted)
                                TextDecoration.LineThrough
                            else TextDecoration.None,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 6.dp)
                        )
                    }

                    // Right side: Action buttons
                    Row {
                        // Edit Button - only show if task is not completed
                        if (!task.isCompleted) {
                            IconButton(onClick = onEdit) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_edit),
                                    contentDescription = "Edit Task",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        IconButton(onClick = onClose) {
                            Icon(
                                painter = painterResource(R.drawable.ic_close),
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // -----------------------------
                // DESCRIPTION
                // -----------------------------
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (task.isCompleted) {
                            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                        },
                        textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // -----------------------------
                // METADATA ITEMS
                // -----------------------------
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetadataItem(
                        painter = painterResource(R.drawable.ic_calendar),
                        text = formatDueDateForDisplay(task.dueDate),
                        isCompleted = task.isCompleted
                    )
                    MetadataItem(
                        painter = painterResource(R.drawable.ic_flag),
                        text = "${task.priority.name} Priority",
                        isCompleted = task.isCompleted
                    )
                    MetadataItem(
                        painter = painterResource(R.drawable.ic_folder),
                        text = getRepeatingDisplayText(task.repeating),
                        isCompleted = task.isCompleted
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // -----------------------------
                // ACTION BUTTONS
                // -----------------------------
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    if (!task.isCompleted) {
                        // For incomplete tasks: Mark Complete + Edit + Delete
                        Button(
                            onClick = onMarkComplete,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(58.dp),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(4.dp)
                        ) {
                            Text(
                                text = "Mark as Complete",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Edit button for incomplete tasks
                        OutlinedButton(
                            onClick = onEdit,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                        ) {
                            Text(
                                text = "Edit Task",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    } else {
                        // For completed tasks: Reopen + Edit + Delete
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = { /* Reopen task logic would go here */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                )
                            ) {
                                Text(
                                    text = "Reopen Task",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = onEdit,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp),
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                                )
                            ) {
                                Text(
                                    text = "Edit",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Delete Button
                    OutlinedButton(
                        onClick = onDelete,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    ) {
                        Text(
                            text = "Delete Task",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskDetailScreenCompletedPreview() {
    MaterialTheme {
        TaskDetailScreen(
            task = Task(
                title = "Design Final Mockups",
                description = "Final design review for the new landing page. Ensure all elements are properly aligned and responsive across screen sizes.",
                priority = Priority.HIGH,
                repeating = RepeatType.NONE,
                dueDate = System.currentTimeMillis() + 86400000,
                isCompleted = true
            ),
            onEdit = {}
        )
    }
}