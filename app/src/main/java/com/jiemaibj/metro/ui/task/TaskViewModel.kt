package com.jiemaibj.metro.ui.task

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiemaibj.metro.R
import com.jiemaibj.metro.data.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(@ApplicationContext context: Context) :
    ViewModel() {
    val task = MutableLiveData(
        Task(
            context.getString(R.string.example_task_name),
            context.getString(R.string.example_task_description),
        )
    )
    val dialog = MutableLiveData<String?>()

    fun submit() {
        viewModelScope.launch {
            // encrypts task data here
            val json = Json.encodeToString(task.value)
            // pops up dialog
            dialog.value = json
        }
    }
}