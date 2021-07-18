package com.davek.taskapp.data.local

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.davek.taskapp.MainCoroutineRule
import com.davek.taskapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class TaskDaoTest {

    private lateinit var database: TaskDatabase
    private lateinit var taskDao: TaskDao

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears when the
        // process is killed
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        ).allowMainThreadQueries().build()
        taskDao = database.taskDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun verifyInsertTaskAndGetById() = runBlockingTest {
        val task = Task(
            taskId = 100L,
            title = "Test Task",
            description = "Test Description",
            dueDate = getCalendarInstance().time
        )
        taskDao.insert(task)
        val loaded = taskDao.getTaskById(task.taskId)

        assertThat(loaded as Task, notNullValue())
        assertEquals(task.taskId, loaded.taskId)
        assertEquals(task.title, loaded.title)
        assertEquals(task.description, loaded.description)
        assertEquals(task.dueDate, loaded.dueDate)
    }

    //TODO: add more test cases

    private fun getCalendarInstance(): Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}
