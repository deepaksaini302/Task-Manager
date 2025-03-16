package com.project.taskmanagercopy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project.taskmanagercopy.data.Task
import com.project.taskmanagercopy.ui.TaskItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeableTaskItem(
    task: Task,
    onClick: () -> Unit,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val scope = rememberCoroutineScope()

    val dismissState = rememberDismissState()

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            DismissValue.DismissedToEnd -> {
                onComplete()
                dismissState.snapTo(DismissValue.Default) // Reset state after completing
            }
            DismissValue.DismissedToStart -> {
                val result = snackbarHostState.showSnackbar(
                    message = "Task deleted",
                    actionLabel = "Undo",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    dismissState.snapTo(DismissValue.Default) // Reset if undo clicked
                } else {
                    onDelete()
                }
            }
            else -> {}
        }
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {
            val color = when (dismissState.dismissDirection) {
                DismissDirection.StartToEnd -> Color.LightGray // Complete
                DismissDirection.EndToStart -> Color.DarkGray   // Delete
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 16.dp),
                contentAlignment = if (dismissState.dismissDirection == DismissDirection.EndToStart)
                    Alignment.CenterEnd else Alignment.CenterStart
            ) {
                Icon(
                    imageVector = if (color == Color.Green) Icons.Default.Check else Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        },
        dismissContent = {
            TaskItem(
                task = task,
                onClick = onClick
            )
        }
    )
}