package com.dailyease.app.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dailyease.app.R
import com.dailyease.app.data.model.Priority
import com.dailyease.app.data.model.RepeatType
import com.dailyease.app.data.model.Task
import com.dailyease.app.ui.components.DatePickerDialog
import com.dailyease.app.util.formatDueDateForDisplay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onClose: () -> Unit,
    onSaveTask: (Task) -> Unit
) {
    var taskTitle by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(Priority.MEDIUM) }
    var selectedRepeating by remember { mutableStateOf(RepeatType.NONE) }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.White, // Changed from Transparent
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
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
                            MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(3.dp)
                        )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)) {
                    Text(
                        text = "Add New Task",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = "Close",
                            tint = Color.Gray // FIXED: Visible color
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text("Task Title") },
                    placeholder = { Text("e.g. Design meeting slides") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    placeholder = { Text("Add more details about your task") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = false // Added this line
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Due Date
                Text(
                    text = "Due Date",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_calendar),
                        contentDescription = "Select date",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = dueDate?.let { formatDueDateForDisplay(it) } ?: "Select Date",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Repeating
                Text(
                    text = "Repeats",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(RepeatType.entries) { repeat ->
                        AnimatedRepeatingChip(
                            repeatType = repeat,
                            isSelected = selectedRepeating == repeat,
                            onSelected = { selectedRepeating = repeat }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Priority
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Priority.entries.forEach { priority ->
                        AnimatedPriorityChip(
                            priority = priority,
                            isSelected = selectedPriority == priority,
                            onSelected = { selectedPriority = priority }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (taskTitle.isNotBlank()) {
                            onSaveTask(
                                Task(
                                    title = taskTitle,
                                    description = description,
                                    priority = selectedPriority,
                                    repeating = selectedRepeating,
                                    dueDate = dueDate ?: (System.currentTimeMillis() + 86400000)
                                )
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(16.dp),
                    enabled = taskTitle.isNotBlank()
                ) {
                    Text("Save Task", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Date Picker
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = {
                dueDate = it
                showDatePicker = false
            }
        )
    }
}

// Animated Repeating Chip
@Composable
fun AnimatedRepeatingChip(
    repeatType: RepeatType,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    )

    FilterChip(
        selected = isSelected,
        onClick = onSelected,
        label = {
            Text(
                when (repeatType) {
                    RepeatType.NONE -> "None"
                    RepeatType.DAILY -> "Daily"
                    RepeatType.WEEKLY -> "Weekly"
                },
                color = textColor
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = backgroundColor,
            labelColor = textColor
        )
    )
}

// Animated Priority Chip
@Composable
fun AnimatedPriorityChip(
    priority: Priority,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            when (priority) {
                Priority.HIGH -> Color(0xFFFF5252)
                Priority.MEDIUM -> Color(0xFFFFB74D)
                Priority.LOW -> Color(0xFF4CAF50)
            }
        } else Color.Transparent
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    )

    TextButton(
        onClick = onSelected,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = backgroundColor,
            contentColor = textColor
        )
    ) {
        Text(priority.name)
    }
}

@Preview(showBackground = true)
@Composable
fun AddTaskScreenPreview() {
    MaterialTheme {
        AddTaskScreen(
            onClose = {},
            onSaveTask = {}
        )
    }
}