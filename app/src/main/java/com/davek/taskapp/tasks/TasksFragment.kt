package com.davek.taskapp.tasks

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.davek.taskapp.R
import com.davek.taskapp.databinding.FragmentTasksBinding

class TasksFragment : Fragment() {

    private lateinit var binding: FragmentTasksBinding
    private val tasksViewModel: TasksViewModel by viewModels()

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

        return binding.root
    }

    private fun subscribeViewModel() {
        tasksViewModel.navigateToTaskDetail.observe(viewLifecycleOwner) {
            findNavController().navigate(
                TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment()
            )
        }
    }

}