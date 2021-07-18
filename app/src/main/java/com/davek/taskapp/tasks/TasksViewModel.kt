package com.davek.taskapp.tasks

import android.util.Log
import androidx.lifecycle.*
import com.davek.taskapp.data.DataResult
import com.davek.taskapp.data.Task
import com.davek.taskapp.data.TaskRepository
import com.davek.taskapp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val taskRepository: TaskRepository
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
//        _navigateToTaskDetail.call()
        //TODO: remove temp code
        val task = Task(
            title = "Test Task",
            description = "Test Description",
            dueDate = getCalendarInstance().time
        )
        insertTask(task)
    }

    fun onTaskClick(taskId: Long) {
        _navigateToTaskDetail.value = taskId
    }

    private fun getCalendarInstance(): Calendar = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    private fun insertTask(task: Task) = viewModelScope.launch {
        taskRepository.insertTask(task)
    }
}