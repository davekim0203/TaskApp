package com.davek.taskapp.taskdetail

import androidx.lifecycle.ViewModel
import com.davek.taskapp.data.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {
    //TODO
}