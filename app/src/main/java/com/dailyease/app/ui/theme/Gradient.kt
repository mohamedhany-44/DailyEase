package com.dailyease.app.ui.theme

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.gradientBackground(): Modifier {
    return this
        .background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFB388FF), // Light Purple
                    Color(0xFF82B1FF)  // Light Blue
                )
            )
        )
        .alpha(0.8f) // Apply alpha to the whole Modifier
}
