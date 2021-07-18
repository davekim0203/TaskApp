package com.davek.taskapp.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davek.taskapp.R
import com.davek.taskapp.data.Task
import com.davek.taskapp.tasks.TaskAdapter
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("listItemTaskDueDate")
fun TextView.setListItemTaskDueDateText(task: Task?) {
    task?.let {
        text = String.format(
            resources.getString(R.string.list_item_task_due_date),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.dueDate)
        )
    }
}

@BindingAdapter("taskDueDate")
fun TextView.setTaskDueDateText(date: Date?) {
    date?.let {
        text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
    }
}

@BindingAdapter("tasks")
fun setTasks(listView: RecyclerView, tasks: List<Task>?) {
    tasks?.let {
        (listView.adapter as TaskAdapter).data = it
    }
}