package com.davek.taskapp.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.*

class DefaultTaskRepository constructor(
    private val taskDataSource: TaskDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskRepository {

    override fun observeTasks(): LiveData<DataResult<List<Task>>> {
        return taskDataSource.observeTasks()
    }

    override suspend fun insertTask(task: Task): DataResult<Long> {
        return taskDataSource.insertTask(task)
    }

    override suspend fun updateTask(task: Task) {
        withContext(ioDispatcher) {
            launch {
                taskDataSource.updateTask(task)
            }
        }
    }

    override suspend fun deleteTaskById(taskId: Long) {
        withContext(ioDispatcher) {
            launch {
                taskDataSource.deleteTaskById(taskId)
            }
        }
    }

    override suspend fun getTaskById(taskId: Long): DataResult<Task> {
        return taskDataSource.getTask(taskId)
    }
}
