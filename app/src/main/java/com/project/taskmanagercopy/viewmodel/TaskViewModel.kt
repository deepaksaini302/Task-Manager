package com.project.taskmanagercopy.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.taskmanagercopy.data.Task
import com.project.taskmanagercopy.database.TaskDatabase
import com.project.taskmanagercopy.repository.TaskRepository
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TaskRepository
    val allTasks: LiveData<List<Task>>

    init {
        val taskDao = TaskDatabase.getDatabase(application).taskDao()
        repository = TaskRepository(taskDao)
        allTasks = repository.allTasks.asLiveData()
    }

    // ✅ Get Task by ID for Details Screen
    fun getTaskById(taskId: Int): LiveData<Task> {
        return repository.getTaskById(taskId)
    }

    // ✅ Mark Task as Completed
    fun markTaskAsCompleted(task: Task) = viewModelScope.launch {
        val updatedTask = task.copy(isCompleted = true)
        repository.update(updatedTask)
    }


    fun addTask(task: Task) = viewModelScope.launch {
        repository.insert(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.update(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.delete(task)
    }
}