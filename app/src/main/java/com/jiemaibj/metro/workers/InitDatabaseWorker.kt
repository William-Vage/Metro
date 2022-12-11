package com.jiemaibj.metro.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.jiemaibj.metro.R
import com.jiemaibj.metro.data.AppDatabase
import com.jiemaibj.metro.data.model.NavMenu
import com.jiemaibj.metro.data.model.Permissions
import com.jiemaibj.metro.utilities.INT_APP_HANDLE_OVERHAUL
import com.jiemaibj.metro.utilities.INT_APP_SETTING
import com.jiemaibj.metro.utilities.INT_APP_TRACK_OVERHAUL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InitDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val filename = inputData.getString(KEY_FILENAME)
            if (filename != null) {
                applicationContext.assets.open(filename).use { inputStream ->
                    JsonReader(inputStream.reader()).use { jsonReader ->

                        val permissions: Permissions =
                            Gson().fromJson(jsonReader, Permissions::class.java)

//                        val database = AppDatabase.getInstance(applicationContext)
//                        database.navMenuDao().initData(getMenus(permissions.menus))
                        Result.success()
                    }
                }
                Result.success()
            } else {
                Result.failure()
            }

        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun getMenus(permissionsList: List<Int>): List<NavMenu> {
        val menus = mutableListOf<NavMenu>()
        for (permissions in permissionsList) {
            when (permissions) {
                INT_APP_TRACK_OVERHAUL -> {
                    menus.add(NavMenu(name = "跟踪检修任务", iconId = R.drawable.ic_password))
                }
                INT_APP_HANDLE_OVERHAUL -> {
                    menus.add(NavMenu(name = "检修工作流程", iconId = R.drawable.ic_password))
                }
                INT_APP_SETTING -> {
                    menus.add(NavMenu(name = "设置", iconId = R.drawable.ic_setting_menu))
                }
            }
        }
        return menus
    }

    companion object {
        const val KEY_FILENAME = "DATA_FILENAME"
    }
}