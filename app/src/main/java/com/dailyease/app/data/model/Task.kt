// Task.kt
package com.dailyease.app.data.model

import kotlin.random.Random
import java.io.Serializable

data class Task(
    val title: String,
    val description: String = "",
    val priority: Priority = Priority.MEDIUM,
    val repeating: RepeatType = RepeatType.NONE,
    val dueDate: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false,
    val id: Long = Random.nextLong(0L, 500000L)
) : Serializable