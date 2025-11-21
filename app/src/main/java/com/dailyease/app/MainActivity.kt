package com.dailyease.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dailyease.app.data.model.Habit
import com.dailyease.app.ui.screens.*
import com.dailyease.app.ui.theme.DailyEaseTheme
import com.dailyease.app.viewmodel.DailyTasksViewModel
import com.dailyease.app.viewmodel.HabitsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DailyEaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainApp()
                }
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()

    // Define your screens
    val tasksScreen = "tasks"
    val habitsScreen = "habits"

    var selectedScreen by remember { mutableStateOf(tasksScreen) }

    // Simple Bottom Navigation Scaffold
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_task),
                            contentDescription = "Tasks"
                        )
                    },
                    label = { Text("Tasks") },
                    selected = selectedScreen == tasksScreen,
                    onClick = {
                        selectedScreen = tasksScreen
                        navController.navigate(tasksScreen) {
                            popUpTo(tasksScreen) { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_habit),
                            contentDescription = "Habits"
                        )
                    },
                    label = { Text("Habits") },
                    selected = selectedScreen == habitsScreen,
                    onClick = {
                        selectedScreen = habitsScreen
                        navController.navigate(habitsScreen) {
                            popUpTo(habitsScreen) { inclusive = true }
                        }
                    }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = tasksScreen,
            modifier = Modifier.padding(padding)
        ) {
            composable(tasksScreen) {
                TasksApp()
            }
            composable(habitsScreen) {
                HabitsApp()
            }
        }
    }
}

@Composable
fun TasksApp(viewModel: DailyTasksViewModel = viewModel()) {
    val tasks = viewModel.tasksStateList
    val selectedTask = viewModel.selectedTask.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val showAddTask = remember { mutableStateOf(false) }
    val showEditTask = remember { mutableStateOf(false) }

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
                            viewModel.setSelectedTask(updatedTask)
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
                            showEditTask.value = true
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

@Composable
fun HabitsApp(viewModel: HabitsViewModel = viewModel()) {
    val habits by viewModel.habits.collectAsState()
    val todayProgress by viewModel.todayProgress.collectAsState()
    val bestHabit by viewModel.bestHabit.collectAsState()
    val streakCount by viewModel.streakCount.collectAsState()

    var showEditHabit by remember { mutableStateOf(false) }
    var selectedHabit by remember { mutableStateOf<Habit?>(null) }

    HabitScreen(
        habits = habits,
        onAddHabit = { name, target, icon ->
            viewModel.addHabit(name, target, icon)
        },
        onIncrementProgress = { habitId ->
            viewModel.incrementProgress(habitId)
        },
        onEditHabit = { habit ->
            selectedHabit = habit
            showEditHabit = true
        },
        onDeleteHabit = { habitId -> // This should work now
            viewModel.deleteHabit(habitId)
        },
        onResetProgress = {
            viewModel.resetAllProgress()
        },
        todayProgress = todayProgress,
        bestHabit = bestHabit,
        streakCount = streakCount
    )

    // Edit Habit Bottom Sheet - REMOVE the onDeleteHabit parameter since it's not in the function signature
    if (showEditHabit && selectedHabit != null) {
        EditHabitScreen(
            habitId = selectedHabit!!.id,
            currentName = selectedHabit!!.name,
            currentTarget = selectedHabit!!.dailyTarget,
            currentIcon = selectedHabit!!.icon,
            onDismiss = {
                showEditHabit = false
                selectedHabit = null
            },
            onUpdateHabit = { habitId, name, target, icon ->
                viewModel.updateHabit(habitId, name, target, icon)
                showEditHabit = false
                selectedHabit = null
            }
            // Remove the onDeleteHabit parameter since EditHabitScreen doesn't have it
        )
    }
}