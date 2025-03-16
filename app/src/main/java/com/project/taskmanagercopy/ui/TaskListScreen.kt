package com.project.taskmanagercopy.ui

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.taskmanagercopy.ui.components.EmptyStateUI
import com.project.taskmanagercopy.ui.components.SwipeableTaskItem
import com.project.taskmanagercopy.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(navController: NavController, viewModel: TaskViewModel, context: Context) {
    val tasks by viewModel.allTasks.observeAsState(emptyList())
    var selectedPriority by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var selectedDate by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Apply filters and sorting
    var filteredTasks = tasks
    selectedPriority?.let { priority ->
        filteredTasks = filteredTasks.filter { it.priority == priority }
    }
    selectedStatus?.let { status ->
        filteredTasks = when (status) {
            "Completed" -> filteredTasks.filter { it.isCompleted }
            "Pending" -> filteredTasks.filter { !it.isCompleted }
            else -> filteredTasks
        }
    }
    selectedDate?.let { date ->
        filteredTasks = filteredTasks.filter { it.dueDate == date }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    // Priority Filter
                    PriorityDropdown(selectedPriority) { selectedPriority = it }

                    // Status Filter
                    StatusDropdown(selectedStatus) { selectedStatus = it }

                    // Date Filter
                    IconButton(onClick = { showDatePicker(context) { date -> selectedDate = date } }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                    }

                    // Clear Filters
                    IconButton(onClick = {
                        selectedPriority = null
                        selectedStatus = null
                        selectedDate = null
                    }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear Filters")
                    }

                    // Settings Button
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_task") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (filteredTasks.isEmpty()) {
                EmptyStateUI()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredTasks, key = { it.id }) { task ->
                        SwipeableTaskItem(
                            task = task,
                            onClick = { navController.navigate("task_details/${task.id}") },
                            onComplete = {
                                viewModel.markTaskAsCompleted(task)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Task completed",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                                         },
                            onDelete = {
                                viewModel.deleteTask(task)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Task deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            },
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }
}

// Priority Dropdown
@Composable
fun PriorityDropdown(selectedPriority: String?, onPrioritySelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Low", "Medium", "High")

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.List, contentDescription = "Sort by Priority")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority) },
                    onClick = {
                        onPrioritySelected(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Status Dropdown
@Composable
fun StatusDropdown(selectedStatus: String?, onStatusSelected: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("All", "Completed", "Pending")

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.List, contentDescription = "Filter by Status")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status) },
                    onClick = {
                        onStatusSelected(if (status == "All") null else status)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Show date picker for filtering
fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, day ->
            val formattedDate = "$day/${month + 1}/$year"
            onDateSelected(formattedDate)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()

}