package com.dailyease.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.dailyease.app.data.model.Task
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class DataManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("daily_ease_prefs", Context.MODE_PRIVATE)
    }
    private val gson = Gson()
    private val TASKS_KEY = "tasks_list"

    fun saveTasks(tasks: List<Task>) {
        val tasksJson = gson.toJson(tasks)
        sharedPreferences.edit { putString(TASKS_KEY, tasksJson) }
    }

    fun loadTasks(): List<Task> {
        val tasksJson = sharedPreferences.getString(TASKS_KEY, null)
        return if (tasksJson != null) {
            try {
                val type = object : TypeToken<List<Task>>() {}.type
                gson.fromJson<List<Task>>(tasksJson, type) ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        } else {
            emptyList()
        }
    }

    fun clearAllData() {
        sharedPreferences.edit { clear() }
    }
}