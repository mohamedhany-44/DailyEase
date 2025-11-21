package com.dailyease.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyease.app.R
import com.dailyease.app.data.model.Task
import com.dailyease.app.ui.components.TaskCard

@Composable
fun TasksScreen(
    tasks: List<Task>,
    currentFilter: String,
    onFilterChange: (String) -> Unit,
    onAddTask: () -> Unit = {},
    onTaskClick: (Task) -> Unit,
    onTaskToggle: (Long, Boolean) -> Unit // Add this parameter
) {

    var selectedFilter by remember { mutableStateOf(currentFilter) }

    // Update local filter when currentFilter changes from ViewModel
    LaunchedEffect(currentFilter) {
        selectedFilter = currentFilter
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(6.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add Task",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFB388FF),
                            Color(0xFF82B1FF)
                        )
                    )
                )
        ) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp, vertical = 32.dp)
            ) {
                Text(
                    text = "DailyEase",
                    fontSize = 38.sp,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            // Filter Chips
            val filters = listOf("All", "Today", "Upcoming", "Completed", "LOW", "HIGH", "MEDIUM")
            LazyRow(
                modifier = Modifier.padding(start = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = {
                            selectedFilter = filter
                            onFilterChange(filter)
                        },
                        label = {
                            Text(
                                filter,
                                fontWeight =
                                    if (selectedFilter == filter) FontWeight.Bold
                                    else FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = if (selectedFilter == filter)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface,
                            labelColor = if (selectedFilter == filter)
                                Color.White
                            else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Task Section Title
            Text(
                text = "Your Tasks",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Task List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(tasks) { task ->
                    TaskCard(
                        task = task,
                        onTaskClick = { onTaskClick(task) },
                        onTaskCompleted = { taskId, isCompleted ->
                            onTaskToggle(taskId, isCompleted)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksScreenPreview() {
    MaterialTheme {
        TasksScreen(
            tasks = emptyList(),
            currentFilter = "All",
            onFilterChange = {},
            onAddTask = {},
            onTaskClick = {},
            onTaskToggle = { _, _ -> }
        )
    }
}