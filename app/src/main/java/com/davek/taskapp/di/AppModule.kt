package com.davek.taskapp.di

import android.content.Context
import androidx.room.Room
import com.davek.taskapp.data.DefaultTaskRepository
import com.davek.taskapp.data.TaskDataSource
import com.davek.taskapp.data.TaskRepository
import com.davek.taskapp.data.local.TaskDatabase
import com.davek.taskapp.data.local.TaskLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideTaskDatabase(@ApplicationContext context: Context): TaskDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            "task_table"
        ).build()

    @Singleton
    @Provides
    fun provideTaskLocalDataSource(
        taskDatabase: TaskDatabase,
        ioDispatcher: CoroutineDispatcher
    ): TaskDataSource = TaskLocalDataSource(
        taskDatabase.taskDao(),
        ioDispatcher
    )

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}

/**
 * The binding for TasksRepository is on its own module so
 * that we can replace it easily in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
object TaskRepositoryModule {

    @Singleton
    @Provides
    fun provideTaskRepository(
        taskDataSource: TaskDataSource,
        ioDispatcher: CoroutineDispatcher
    ): TaskRepository = DefaultTaskRepository(
        taskDataSource,
        ioDispatcher
    )
}
