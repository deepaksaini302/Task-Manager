package com.project.taskmanagercopy.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.project.taskmanagercopy.viewmodel.TaskViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: TaskViewModel) {
    NavHost(navController = navController, startDestination = "task_list") {

        // Home Screen - Task List
        composable("task_list") {
            TaskListScreen(navController, viewModel,context = LocalContext.current)
        }

        // Add Task Screen
        composable("add_task") {
            AddTaskScreen(navController, viewModel)
        }

        // Task Details Screen
        composable("task_details/{taskId}", arguments = listOf(navArgument("taskId") { type = NavType.IntType })) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            TaskDetailsScreen(navController, viewModel, taskId)
        }

        // Settings Screen
        composable("settings") {
            SettingsScreen(navController, viewModel)
        }
    }
}