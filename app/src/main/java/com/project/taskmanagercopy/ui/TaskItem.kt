package com.project.taskmanagercopy.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.project.taskmanagercopy.data.Task

@Composable
fun TaskItem(
    task: Task,
    onClick: () -> Unit,
) {
    val backgroundColor = Color(0xFFF5C9E2) // Light pink background

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Due: ${task.dueDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Text(
                    text = "Status: ${if (task.isCompleted) "Completed" else "Pending"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (task.isCompleted) Color(0xFF4CAF50) else Color.Red
                )

                task.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))


        }
    }
}
