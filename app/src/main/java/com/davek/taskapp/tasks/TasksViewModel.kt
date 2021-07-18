package com.davek.taskapp.tasks

import androidx.lifecycle.*
import com.davek.taskapp.data.Task
import com.davek.taskapp.util.SingleLiveEvent
import java.util.*

class TasksViewModel : ViewModel() {

    private val _navigateToTaskDetail = SingleLiveEvent<Any>()
    val navigateToTaskDetail: LiveData<Any>
        get() = _navigateToTaskDetail

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>>
        get() = _tasks

    init {
        //TODO: remove temp data
        _tasks.value = listOf(
            Task(1L, "Test title", "test description", getCalendarInstance().time),
            Task(1L, "Test title 2", "test description", getCalendarInstance().time),
            Task(1L, "Test title 3", "Longer description Longer description Longer description Longer description Longer description Longer description Longer description ", getCalendarInstance().time)
        )
    }

    fun onAddButtonClick() {
        _navigateToTaskDetail.call()
    }

    fun onTaskClick(taskId: Long) {
        //TODO
    }

    private fun getCalendarInstance(): Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}