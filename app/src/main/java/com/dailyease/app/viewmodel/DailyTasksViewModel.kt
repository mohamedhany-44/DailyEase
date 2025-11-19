package com.dailyease.app.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dailyease.app.data.local.DataManager
import com.dailyease.app.data.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class DailyTasksViewModel(application: Application) : AndroidViewModel(application) {

    private val dataManager = DataManager(application.applicationContext)

    // Use MutableStateList for reactive updates
    private val _tasksStateList = mutableStateListOf<Task>()
    val tasksStateList: List<Task> get() = _tasksStateList

    private val _selectedTask = MutableStateFlow<Task?>(null)
    val selectedTask: StateFlow<Task?> = _selectedTask.asStateFlow()

    private val _currentFilter = MutableStateFlow("All")
    val currentFilter: StateFlow<String> = _currentFilter.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTasksFromStorage()
    }

    private fun loadTasksFromStorage() {
        viewModelScope.launch {
            _isLoading.value = true
            val tasks = dataManager.loadTasks()
            _tasksStateList.clear()
            _tasksStateList.addAll(tasks)

            _isLoading.value = false
        }
    }

    private fun saveTasksToStorage() {
        viewModelScope.launch {
            dataManager.saveTasks(_tasksStateList.toList())
        }
    }

    fun addTask(task: Task) {
        _tasksStateList.add(0, task) // Add to top of list
        saveTasksToStorage()
    }

    fun updateTask(updatedTask: Task) {
        val index = _tasksStateList.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            _tasksStateList[index] = updatedTask
            saveTasksToStorage()
        }
    }

    fun deleteTask(taskId: Long) {
        _tasksStateList.removeAll { it.id == taskId }
        saveTasksToStorage()
    }

    fun setSelectedTask(task: Task?) {
        _selectedTask.value = task
    }

    fun setFilter(filter: String) {
        _currentFilter.value = filter
    }

    // Updated toggle function - much simpler
    fun toggleTaskCompletion(taskId: Long, isCompleted: Boolean) {
        val index = _tasksStateList.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val task = _tasksStateList[index]
            _tasksStateList[index] = task.copy(isCompleted = isCompleted)
            saveTasksToStorage()
        }
    }

    // Keep this for backward compatibility with detail screen
    fun markTaskComplete(taskId: Long) {
        toggleTaskCompletion(taskId, true)
    }

    fun getFilteredTasks(): List<Task> {
        val calendar = Calendar.getInstance()
        val todayStart = calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val todayEnd = todayStart + 86400000 // 24 hours

        return when (_currentFilter.value) {
            "Today" -> _tasksStateList.filter {
                it.dueDate in todayStart..todayEnd && !it.isCompleted
            }

            "Upcoming" -> _tasksStateList.filter {
                it.dueDate > System.currentTimeMillis() && !it.isCompleted
            }

            "Completed" -> _tasksStateList.filter { it.isCompleted }
            "LOW" -> _tasksStateList.filter { it.priority.name == "LOW" }
            "MEDIUM" -> _tasksStateList.filter { it.priority.name == "MEDIUM" }
            "HIGH" -> _tasksStateList.filter { it.priority.name == "HIGH" }
            else -> _tasksStateList.toList() // "All"
        }
    }

}