package com.davek.taskapp.tasks

import androidx.lifecycle.*
import com.davek.taskapp.util.SingleLiveEvent

class TasksViewModel : ViewModel() {

    private val _navigateToTaskDetail = SingleLiveEvent<Any>()
    val navigateToTaskDetail: LiveData<Any>
        get() = _navigateToTaskDetail

    fun onAddButtonClick() {
        _navigateToTaskDetail.call()
    }
}