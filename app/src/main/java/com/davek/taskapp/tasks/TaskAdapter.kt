package com.davek.taskapp.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davek.taskapp.data.Task
import com.davek.taskapp.databinding.ListItemTaskBinding

class TaskAdapter(private val viewModel: TasksViewModel) :
    ListAdapter<Task, TaskAdapter.ViewHolder>(TaskDiffCallback()) {

    var data = listOf<Task>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

//    fun getTaskByPosition(position: Int): Task {
//        return data[position]
//    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = data[position]
        holder.bind(viewModel, task)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(
        private val binding: ListItemTaskBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: TasksViewModel, task: Task) {
            binding.tasksVm = viewModel
            binding.task = task
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemTaskBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.taskId == newItem.taskId
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}