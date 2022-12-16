package com.jiemaibj.metro.ui.task

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jiemaibj.metro.R
import com.jiemaibj.metro.data.model.Task
import com.jiemaibj.metro.utilities.toBase64String
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import com.jiemaibj.metro.utils.SM4Util
import com.jiemaibj.metro.utils.DESUtil

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

    fun submit(view: View) {
        val bitmap = BitmapFactory.decodeResource(view.context.resources, R.drawable.main_cover)
        val image = ByteArrayOutputStream().let {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            it.toByteArray().toBase64String()
        }
        viewModelScope.launch {
            // encrypts task data here
            task.value = task.value!!.copy(image = image)
            val json = Json.encodeToString(task.value)


            var startTime = System.currentTimeMillis()
            var sm4 = SM4Util()
            val sm4Encrypt = sm4.encrypt(json)
            var endTime = System.currentTimeMillis()
            var usedTime = (endTime - startTime)
            val sm4String = "SM4加密耗时:${usedTime} ms"

            startTime = System.currentTimeMillis()
            var des = DESUtil()
            val desEncrypt = des.encrypt(json)
            endTime = System.currentTimeMillis()
            usedTime = (endTime - startTime)
            val desString = "DES加密耗时:${usedTime} ms"

            val print = "任务信息与任务图片加密完成\n${sm4String}\n${desString}"
            // pops up dialog
            dialog.value = print


        }
    }
}