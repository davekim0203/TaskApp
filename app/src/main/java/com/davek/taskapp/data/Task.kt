package com.davek.taskapp.data

import java.util.*

data class Task(

    var taskId: Long = 0L,

    var title: String,

    var description: String? = null,

    var dueDate: Date
)
