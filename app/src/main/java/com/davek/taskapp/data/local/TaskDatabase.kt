package com.davek.taskapp.data.local

import androidx.room.*
import com.davek.taskapp.data.Converters
import com.davek.taskapp.data.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
