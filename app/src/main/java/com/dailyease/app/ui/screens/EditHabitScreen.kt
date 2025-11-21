package com.dailyease.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dailyease.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHabitScreen(
    habitId: Long,
    currentName: String,
    currentTarget: Int,
    currentIcon: Int,
    onDismiss: () -> Unit,
    onUpdateHabit: (Long, String, Int, Int) -> Unit,
) {
    var habitName by remember { mutableStateOf(currentName) }
    var dailyTarget by remember { mutableStateOf(currentTarget.toString()) }
    var selectedIcon by remember { mutableStateOf<Int?>(currentIcon) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 36.dp, height = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with Delete button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Edit Habit",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            // Habit Name Section
            Text(
                text = "Habit Name",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = habitName,
                onValueChange = { habitName = it },
                placeholder = { Text("e.g., Meditate") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Daily Target Section
            Text(
                text = "Daily Target",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = dailyTarget,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 3) {
                        dailyTarget = newValue
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                trailingIcon = { Text("times") }
            )

            // Choose Icon Section
            Text(
                text = "Choose Icon",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            val icons = listOf(
                R.drawable.ic_run,
                R.drawable.ic_read,
                R.drawable.ic_water,
                R.drawable.ic_self,
                R.drawable.ic_workout
            )

            LazyRow(
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                items(icons) { icon ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    ) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            modifier = Modifier
                                .size(55.dp)
                                .background(
                                    if (selectedIcon == icon)
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                                    else
                                        Color.Transparent,
                                    CircleShape
                                )
                                .clickable { selectedIcon = icon }
                                .padding(10.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(30.dp))

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = {
                        if (habitName.isNotBlank() && selectedIcon != null && dailyTarget.isNotBlank()) {
                            onUpdateHabit(habitId, habitName, dailyTarget.toInt(), selectedIcon!!)
                            onDismiss()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = habitName.isNotBlank() && selectedIcon != null && dailyTarget.isNotBlank()
                ) {
                    Text("Update")
                }
            }

            Spacer(Modifier.height(20.dp))
        }
    }

}

@Preview(showBackground = true, backgroundColor = 0xFFF2F2F2)
@Composable
fun EditHabitScreenPreview() {
    MaterialTheme {
        EditHabitScreen(
            habitId = 1L,
            currentName = "Morning Run",
            currentTarget = 1,
            currentIcon = R.drawable.ic_run,
            onDismiss = {},
            onUpdateHabit = { _, _, _, _ -> },
        )
    }
}