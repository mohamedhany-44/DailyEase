package com.dailyease.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dailyease.app.data.model.Habit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HabitsViewModel : ViewModel() {
    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits.asStateFlow()

    private val _todayProgress = MutableStateFlow("0%")
    val todayProgress: StateFlow<String> = _todayProgress.asStateFlow()

    private val _bestHabit = MutableStateFlow<Habit?>(null)
    val bestHabit: StateFlow<Habit?> = _bestHabit.asStateFlow()

    private val _streakCount = MutableStateFlow(0)
    val streakCount: StateFlow<Int> = _streakCount.asStateFlow()

    init {
        updateAnalytics()
    }

    fun addHabit(name: String, target: Int, icon: Int) {
        viewModelScope.launch {
            val newHabit = Habit(
                id = System.currentTimeMillis(),
                name = name,
                dailyTarget = target,
                icon = icon,
                currentProgress = 0
            )
            _habits.update { currentHabits ->
                currentHabits + newHabit
            }
            updateAnalytics()
        }
    }

    fun incrementProgress(habitId: Long) {
        viewModelScope.launch {
            _habits.update { currentHabits ->
                currentHabits.map { habit ->
                    if (habit.id == habitId && habit.currentProgress < habit.dailyTarget) {
                        habit.copy(currentProgress = habit.currentProgress + 1)
                    } else {
                        habit
                    }
                }
            }
            updateAnalytics()
        }
    }

    fun updateHabit(habitId: Long, name: String, target: Int, icon: Int) {
        viewModelScope.launch {
            _habits.update { currentHabits ->
                currentHabits.map { habit ->
                    if (habit.id == habitId) {
                        habit.copy(
                            name = name,
                            dailyTarget = target,
                            icon = icon
                        )
                    } else {
                        habit
                    }
                }
            }
            updateAnalytics()
        }
    }

    fun deleteHabit(habitId: Long) {
        viewModelScope.launch {
            _habits.update { currentHabits ->
                currentHabits.filter { it.id != habitId }
            }
            updateAnalytics()
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            _habits.update { currentHabits ->
                currentHabits.map { habit ->
                    habit.copy(currentProgress = 0)
                }
            }
            updateAnalytics()
        }
    }

    private fun updateAnalytics() {
        viewModelScope.launch {
            val currentHabits = _habits.value

            // Calculate today's progress
            if (currentHabits.isEmpty()) {
                _todayProgress.value = "0%"
            } else {
                val totalProgress = currentHabits.sumOf { it.currentProgress }
                val totalTarget = currentHabits.sumOf { it.dailyTarget }
                val percentage = if (totalTarget > 0) {
                    (totalProgress.toFloat() / totalTarget * 100).toInt()
                } else {
                    0
                }
                _todayProgress.value = "$percentage%"
            }

            // Find best habit (most completions or highest streak)
            _bestHabit.value = currentHabits.maxByOrNull { it.currentProgress }

            // Calculate streak (simplified - you might want more complex logic)
            _streakCount.value = currentHabits.maxOfOrNull { it.currentProgress } ?: 0
        }
    }
}