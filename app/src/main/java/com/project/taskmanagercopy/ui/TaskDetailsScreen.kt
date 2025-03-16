package com.project.taskmanagercopy.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.taskmanagercopy.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailsScreen(navController: NavController, viewModel: TaskViewModel, taskId: Int) {
    val task = viewModel.getTaskById(taskId).observeAsState().value
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val backgroundColor = Color(0xFFF5C9E2) // Light pink background


    task?.let {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text("Task Details")},
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.deleteTask(it)
                        navController.popBackStack()
                        scope.launch {
                            snackbarHostState.showSnackbar("Task deleted", duration = SnackbarDuration.Short)
                        }
                    },
                    containerColor = Color.Red
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Task", tint = Color.White)
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = it.title,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Priority: ${it.priority}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = when (it.priority) {
                                "High" -> Color.Red
                                "Medium" -> Color.Yellow // Amber
                                "Low" -> Color.Green
                                else -> Color.Black
                            }
                        )
                        Text(
                            text = "Due Date: ${it.dueDate}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = Color.Gray.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = it.description ?: "No Description Available",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.markTaskAsCompleted(it)
                        scope.launch {
                            snackbarHostState.showSnackbar("Task completed", duration = SnackbarDuration.Short)
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)) // Green button
                ) {
                    Text("Mark as Completed", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    } ?: Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Task Not Found", style = MaterialTheme.typography.headlineSmall)
    }
}