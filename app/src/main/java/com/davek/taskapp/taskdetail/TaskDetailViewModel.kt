package com.davek.taskapp.taskdetail

import android.util.Log
import androidx.lifecycle.*
import com.davek.taskapp.data.DataResult
import com.davek.taskapp.data.Task
import com.davek.taskapp.data.TaskRepository
import com.davek.taskapp.tasks.TasksViewModel.Companion.DEFAULT_TASK_ID
import com.davek.taskapp.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {

    private var taskId: Long = DEFAULT_TASK_ID

    private val _showSnackbar = SingleLiveEvent<Int>()
    val showSnackbar: LiveData<Int>
        get() = _showSnackbar

    private val _showDatePicker = SingleLiveEvent<Date>()
    val showDatePicker: LiveData<Date>
        get() = _showDatePicker

    private val _navigateToTasks = SingleLiveEvent<Any>()
    val navigateToTasks: LiveData<Any>
        get() = _navigateToTasks

    /**
     * Exposing MutableLiveData for two-way data binding
     * Should be only set from xml or test
     */
    val taskTitle = MutableLiveData<String>()
    val taskDescription = MutableLiveData<String?>()

    private val _dueDate = MutableLiveData<Date>()
    val dueDate: LiveData<Date>
        get() = _dueDate

    private val selectedDateCalendar: Calendar = getCalendarInstance()
    private var isNewTask: Boolean = false
    private var isTaskLoaded: Boolean = false

    fun loadTask(id: Long) {
        if(isTaskLoaded) return
        if (id == DEFAULT_TASK_ID) {
            _dueDate.value = selectedDateCalendar.time
            isNewTask = true
            return
        }

        taskId = id

        viewModelScope.launch {
            taskRepository.getTaskById(id).let {
                if (it is DataResult.Success) {
                    onTaskLoaded(it.data)
                } else {
                    Log.e("TaskDetailViewModel", "Data loading is not successful")
                }
            }
        }
    }

    private fun onTaskLoaded(task: Task) {
        taskTitle.value = task.title
        selectedDateCalendar.time = task.dueDate
        _dueDate.value = task.dueDate
        taskDescription.value = task.description
        isTaskLoaded = true
    }

    fun onSaveButtonClick() {
        val currentTaskTitle = taskTitle.value

        if (currentTaskTitle == null || currentTaskTitle == "") {
            _showSnackbar.value = SNACKBAR_ID_TITLE_REQUIRED
            return
        }

        val taskToSave = Task(
            title = currentTaskTitle,
            dueDate = selectedDateCalendar.time,
            description = taskDescription.value
        )

        if (isNewTask) {
            insertTask(taskToSave)
        } else {
            taskToSave.taskId = taskId
            updateTask(taskToSave)
        }

        _navigateToTasks.call()
    }

    fun onDateClick() {
        _showDatePicker.value = selectedDateCalendar.time
    }

    fun onDateSelected(year: Int, month: Int, day: Int) {
        selectedDateCalendar.set(year, month, day)
        _dueDate.value = selectedDateCalendar.time
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

    private fun updateTask(task: Task) = viewModelScope.launch {
        taskRepository.updateTask(task)
    }

    fun deleteTask() = viewModelScope.launch {
        if (taskId != DEFAULT_TASK_ID) {
            taskRepository.deleteTaskById(taskId)
            _navigateToTasks.call()
            _showSnackbar.value = SNACKBAR_ID_TASK_DELETED
        }
    }

    companion object {
        const val SNACKBAR_ID_TASK_DELETED: Int = 0
        const val SNACKBAR_ID_TITLE_REQUIRED: Int = 1
    }
}