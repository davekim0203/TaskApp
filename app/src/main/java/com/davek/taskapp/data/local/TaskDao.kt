package com.davek.taskapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.davek.taskapp.data.Task

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * FROM task_table WHERE taskId = :taskId")
    suspend fun getTaskById(taskId: Long): Task?

    @Query("SELECT * FROM task_table ORDER BY taskId")
    fun observeTasks(): LiveData<List<Task>>

    @Query("DELETE FROM task_table WHERE taskId = :taskId")
    suspend fun deleteTaskById(taskId: Long)
}
