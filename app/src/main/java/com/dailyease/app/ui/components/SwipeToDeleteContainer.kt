package com.dailyease.app.ui.components

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.size
import kotlin.math.abs

@Composable
fun SwipeToDeleteContainer(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val density = LocalDensity.current
    val threshold = with(density) { 120.dp.toPx() } // Increased threshold for better UX

    val swipeProgress = (abs(offsetX) / threshold).coerceIn(0f, 1f)

    val transition = updateTransition(targetState = offsetX < -threshold, label = "swipeTransition")

    val cardElevation by transition.animateDp(
        transitionSpec = { spring(stiffness = 300f) },
        label = "cardElevation"
    ) { isSwiped ->
        if (isSwiped) 2.dp else 6.dp
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Delete background that appears as you swipe
        if (offsetX < 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.error.copy(alpha = 0.9f * swipeProgress)
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Swipeable content
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.toInt(), 0) }
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX += delta
                        // Prevent swiping to the right
                        if (offsetX > 0) offsetX = 0f
                    },
                    onDragStopped = {
                        // If swiped beyond threshold, trigger delete
                        if (abs(offsetX) > threshold) {
                            onDelete()
                        } else {
                            // Smoothly return to original position
                            offsetX = 0f
                        }
                    }
                )
        ) {
            content()
        }
    }
}