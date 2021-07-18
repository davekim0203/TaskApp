package com.davek.taskapp.taskdetail

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.davek.taskapp.R
import com.davek.taskapp.databinding.FragmentTaskDetailBinding
import com.davek.taskapp.taskdetail.TaskDetailViewModel.Companion.SNACKBAR_ID_TASK_DELETED
import com.davek.taskapp.taskdetail.TaskDetailViewModel.Companion.SNACKBAR_ID_TITLE_REQUIRED
import com.davek.taskapp.tasks.TasksViewModel.Companion.DEFAULT_TASK_ID
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TaskDetailFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentTaskDetailBinding
    private val taskDetailViewModel: TaskDetailViewModel by viewModels()
    private val args: TaskDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_task_detail, container, false
        )
        binding.taskDetailVm = taskDetailViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        taskDetailViewModel.loadTask(args.taskId)
        setHasOptionsMenu(true)
        subscribeViewModel()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_detail_fragment_menu, menu)
        if (args.taskId == DEFAULT_TASK_ID) {
            menu.findItem(R.id.menu_delete).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_save -> {
                taskDetailViewModel.onSaveButtonClick()
                true
            }
            R.id.menu_delete -> {
                showDeleteTaskDialog()
                true
            }
            else -> false
        }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        taskDetailViewModel.onDateSelected(year, month, dayOfMonth)
    }

    private fun subscribeViewModel() {
        taskDetailViewModel.navigateToTasks.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }


        taskDetailViewModel.showDatePicker.observe(viewLifecycleOwner, {
            showDatePickerDialog(it)
        })

        taskDetailViewModel.showSnackbar.observe(viewLifecycleOwner, {
            when (it) {
                SNACKBAR_ID_TASK_DELETED -> Snackbar.make(
                    binding.root,
                    getString(R.string.snackbar_message_task_deleted),
                    Snackbar.LENGTH_SHORT
                ).show()
                SNACKBAR_ID_TITLE_REQUIRED -> Snackbar.make(
                    binding.root,
                    getString(R.string.snackbar_message_title_required),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showDatePickerDialog(currentDate: Date) {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(requireContext(), this, year, month, day).show()
    }

    private fun showDeleteTaskDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_delete_task_title))
            .setMessage(getString(R.string.dialog_delete_task_message))
            .setPositiveButton(getString(R.string.dialog_delete_task_positive_button)) { dialog, _ ->
                taskDetailViewModel.deleteTask()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_delete_task_negative_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}