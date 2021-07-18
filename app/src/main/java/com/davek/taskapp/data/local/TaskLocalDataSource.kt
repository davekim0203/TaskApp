package com.davek.taskapp.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.davek.taskapp.data.DataResult
import com.davek.taskapp.data.Task
import com.davek.taskapp.data.TaskDataSource
import kotlinx.coroutines.*
import java.lang.Exception

class TaskLocalDataSource constructor(
    private val taskDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TaskDataSource {

    override fun observeTasks(): LiveData<DataResult<List<Task>>> {
        return Transformations.map(
            taskDao.observeTasks()
        ) {
            DataResult.Success(it)
        }
    }

    override suspend fun insertTask(task: Task): DataResult<Long> =
        withContext(ioDispatcher) {
            try {
                val taskId = taskDao.insert(task)
                return@withContext DataResult.Success(taskId)
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }

    override suspend fun updateTask(task: Task) = withContext(ioDispatcher) {
        taskDao.update(task)
    }

    override suspend fun deleteTaskById(taskId: Long) = withContext(ioDispatcher) {
        taskDao.deleteTaskById(taskId)
    }

    override suspend fun getTask(taskId: Long): DataResult<Task> =
        withContext(ioDispatcher) {
            try {
                val task = taskDao.getTaskById(taskId)
                if (task != null) {
                    return@withContext DataResult.Success(task)
                } else {
                    return@withContext DataResult.Error(Exception("Task is not found"))
                }
            } catch (e: Exception) {
                return@withContext DataResult.Error(e)
            }
        }
}
