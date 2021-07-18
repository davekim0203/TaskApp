package com.davek.taskapp.tasks

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.davek.taskapp.R
import com.davek.taskapp.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val tasksViewModel: TasksViewModel by viewModels()
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_tasks, container, false
        )
        binding.tasksVm = tasksViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        subscribeViewModel()
        setupTaskList()

        return binding.root
    }

    private fun subscribeViewModel() {
        tasksViewModel.navigateToTaskDetail.observe(viewLifecycleOwner) {
            findNavController().navigate(
                TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment()
            )
        }
    }

    private fun setupTaskList() {
        val viewModel = binding.tasksVm
        if (viewModel != null) {
            taskAdapter = TaskAdapter(tasksViewModel)
            binding.listTask.adapter = taskAdapter
            binding.listTask.addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        } else {
            Log.e("TasksFragment", "TasksViewModel not initialized when attempting to set up TaskAdapter")
        }
    }

}