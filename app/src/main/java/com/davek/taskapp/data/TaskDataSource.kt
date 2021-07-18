package com.davek.taskapp.data

import androidx.lifecycle.LiveData

interface TaskDataSource {

    fun observeTasks(): LiveData<DataResult<List<Task>>>

    suspend fun insertTask(task: Task): DataResult<Long>

    suspend fun updateTask(task: Task)

    suspend fun deleteTaskById(taskId: Long)

    suspend fun getTask(taskId: Long): DataResult<Task>
}
