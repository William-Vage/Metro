package com.jiemaibj.metro.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jiemaibj.metro.databinding.FragmentTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment() {
    private lateinit var binding: FragmentTaskBinding
    private val taskViewModel by viewModels<TaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater, container, false)
        binding.viewModel = taskViewModel
        binding.executePendingBindings()

        taskViewModel.dialog.observe(viewLifecycleOwner) {
            if (it != null) {
                MaterialAlertDialogBuilder(this@TaskFragment.requireContext())
                    .setTitle("Message")
                    .setMessage(it)
                    .setPositiveButton("OK") { _, _ ->
                        this@TaskFragment.findNavController().popBackStack()
                    }
                    .show()
            }
        }
        return binding.root
    }
}
