package com.dailyease.app.ui.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Composable
fun completionScaleAnimation(isCompleted: Boolean): Modifier {
    val scale = remember { Animatable(1f) }

    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            scale.animateTo(
                targetValue = 0.95f,
                animationSpec = tween(durationMillis = 200)
            )
        } else {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200)
            )
        }
    }

    return Modifier.scale(scale.value)
}