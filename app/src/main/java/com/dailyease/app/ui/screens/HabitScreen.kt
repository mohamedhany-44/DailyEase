package com.dailyease.app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dailyease.app.R
import com.dailyease.app.data.model.Habit
import com.dailyease.app.ui.components.DashboardStatCard
import com.dailyease.app.ui.components.HabitItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(
    habits: List<Habit>,
    onAddHabit: (String, Int, Int) -> Unit,
    onIncrementProgress: (Long) -> Unit,
    onEditHabit: (Habit) -> Unit,
    onDeleteHabit: (Long) -> Unit, // Add this parameter
    onResetProgress: () -> Unit,
    todayProgress: String = "0%",
    bestHabit: Habit? = null,
    streakCount: Int = 0
) {
    rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var habitToDelete by remember { mutableStateOf<Habit?>(null) } // Track which habit to delete

    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { isSheetOpen = true },
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(6.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add Habit",
                    tint = Color.White
                )
            }
        }
    ) { padding ->

        if (isSheetOpen) {
            AddHabitScreen(
                onDismiss = { isSheetOpen = false },
                onSave = { name, target, icon ->
                    onAddHabit(name, target, icon)
                    isSheetOpen = false
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFB388FF),
                            Color(0xFF82B1FF)
                        )
                    )
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "DailyEase",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashboardStatCard(
                    title = "Today's Progress",
                    value = todayProgress,
                    color = Color.Green,
                    modifier = Modifier.weight(1f)
                )
                DashboardStatCard(
                    title = "Best Habit",
                    modifier = Modifier.weight(1f)
                ) {
                    if (bestHabit != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = bestHabit.icon),
                                contentDescription = "Best Habit",
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(bestHabit.name, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Text("No habits", color = Color.Gray)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            DashboardStatCard(
                title = "Streak",
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(R.drawable.ic_streak),
                        contentDescription = "Streak",
                        tint = Color(0xFFECA431),
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(14.dp))
                    Text("$streakCount", style = MaterialTheme.typography.headlineMedium) // Fixed: use streakCount parameter
                }
            }

            Spacer(Modifier.height(18.dp))

            Button(
                onClick = {
                    onResetProgress()
                    Toast.makeText(context, "Progress Reset", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Reset Daily Progress", fontWeight = FontWeight.SemiBold)
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Your Habits",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Display habits list
            Column {
                habits.forEach { habit ->
                    HabitItem(
                        habit = habit,
                        onIncrement = { onIncrementProgress(habit.id) },
                        onEdit = { onEditHabit(habit) },
                        onDelete = {
                            habitToDelete = habit
                            showDeleteDialog = true
                        }
                    )
                }

                // Show message when no habits
                if (habits.isEmpty()) {
                    Text(
                        text = "No habits yet. Tap the + button to add one!",
                        color = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog (moved outside Scaffold for better z-index)
    if (showDeleteDialog && habitToDelete != null) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                habitToDelete = null
            },
            title = { Text("Delete Habit") },
            text = {
                Text("Are you sure you want to delete \"${habitToDelete!!.name}\"? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteHabit(habitToDelete!!.id) // Use the passed callback
                        showDeleteDialog = false
                        habitToDelete = null
                        Toast.makeText(context, "Habit deleted", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        habitToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HabitScreenPreview() {
    MaterialTheme {
        HabitScreen(
            habits = emptyList(),
            onAddHabit = { _, _, _ -> },
            onIncrementProgress = { },
            onEditHabit = { },
            onDeleteHabit = { },
            onResetProgress = { }
        )
    }
}