// MainActivity.kt
package com.dailyease.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dailyease.app.ui.screens.AddTaskScreen
import com.dailyease.app.ui.screens.EditTaskScreen
import com.dailyease.app.ui.screens.EmptyTasksScreen
import com.dailyease.app.ui.screens.TaskDetailScreen
import com.dailyease.app.ui.screens.TasksScreen
import com.dailyease.app.ui.theme.DailyEaseTheme
import com.dailyease.app.viewmodel.DailyTasksViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailyEaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DailyEaseApp()
                }
            }
        }
    }
}

// MainActivity.kt - Update the TasksScreen call
@Composable
fun DailyEaseApp(viewModel: DailyTasksViewModel = viewModel()) {
    val tasks = viewModel.tasksStateList
    val selectedTask = viewModel.selectedTask.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val showAddTask = remember { mutableStateOf(false) }
    val showEditTask = remember { mutableStateOf(false) } // Add this state

    // Show loading state
    if (isLoading) {
        EmptyTasksScreen(
            onAddTask = { showAddTask.value = true }
        )
    } else {
        // Show empty state or tasks list
        if (tasks.isEmpty()) {
            EmptyTasksScreen(
                onAddTask = { showAddTask.value = true }
            )
        } else {
            // Show task detail or tasks list
            if (selectedTask != null) {
                if (showEditTask.value) {
                    // Show Edit Task Screen
                    EditTaskScreen(
                        task = selectedTask,
                        onClose = {
                            showEditTask.value = false
                        },
                        onUpdateTask = { updatedTask ->
                            viewModel.updateTask(updatedTask)
                            showEditTask.value = false
                            viewModel.setSelectedTask(updatedTask) // Update the selected task
                        }
                    )
                } else {
                    // Show Task Detail Screen
                    TaskDetailScreen(
                        task = selectedTask,
                        onClose = { viewModel.setSelectedTask(null) },
                        onMarkComplete = {
                            selectedTask.id.let { viewModel.markTaskComplete(it) }
                            viewModel.setSelectedTask(null)
                        },
                        onDelete = {
                            selectedTask.id.let { viewModel.deleteTask(it) }
                            viewModel.setSelectedTask(null)
                        },
                        onEdit = {
                            showEditTask.value = true // Show edit screen when edit is clicked
                        }
                    )
                }
            } else {
                TasksScreen(
                    tasks = viewModel.getFilteredTasks(),
                    currentFilter = viewModel.currentFilter.collectAsState().value,
                    onFilterChange = { filter -> viewModel.setFilter(filter) },
                    onAddTask = { showAddTask.value = true },
                    onTaskClick = { task -> viewModel.setSelectedTask(task) },
                    onTaskToggle = { taskId, isCompleted ->
                        viewModel.toggleTaskCompletion(taskId, isCompleted)
                    }
                )
            }
        }
    }

    // Add Task Bottom Sheet
    if (showAddTask.value) {
        AddTaskScreen(
            onClose = { showAddTask.value = false },
            onSaveTask = { task ->
                viewModel.addTask(task)
                showAddTask.value = false
            }
        )
    }
}