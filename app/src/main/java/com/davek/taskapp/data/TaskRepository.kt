package com.davek.taskapp.data

import androidx.lifecycle.LiveData

interface TaskRepository {

    fun observeTasks(): LiveData<DataResult<List<Task>>>

    suspend fun insertTask(task: Task): DataResult<Long>

    suspend fun updateTask(task: Task)

    suspend fun deleteTaskById(taskId: Long)

    suspend fun getTaskById(taskId: Long): DataResult<Task>
}
