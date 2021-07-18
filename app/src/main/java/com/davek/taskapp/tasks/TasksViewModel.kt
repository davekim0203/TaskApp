package com.davek.taskapp.tasks

import android.util.Log
import androidx.lifecycle.*
import com.davek.taskapp.data.DataResult
import com.davek.taskapp.data.Task
import com.davek.taskapp.data.TaskRepository
import com.davek.taskapp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    taskRepository: TaskRepository
): ViewModel() {

    private val _tasks: LiveData<List<Task>> =
        Transformations.switchMap(taskRepository.observeTasks()) { result ->
            val items = MutableLiveData<List<Task>>()
            if (result is DataResult.Success) {
                items.value = result.data
            } else {
                items.value = emptyList()
                Log.e("TasksViewModel", "Error while loading tasks")
            }
            items
        }
    val tasks: LiveData<List<Task>>
        get() = _tasks

    private val _navigateToTaskDetail = SingleLiveEvent<Long>()
    val navigateToTaskDetail: LiveData<Long>
        get() = _navigateToTaskDetail

    fun onAddButtonClick() {
        _navigateToTaskDetail.value = DEFAULT_TASK_ID
    }

    fun onTaskClick(taskId: Long) {
        _navigateToTaskDetail.value = taskId
    }

    companion object {
        const val DEFAULT_TASK_ID = -1L
    }
}