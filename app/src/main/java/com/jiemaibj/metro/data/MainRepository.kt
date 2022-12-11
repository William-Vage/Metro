package com.jiemaibj.metro.data

import com.jiemaibj.metro.R
import com.jiemaibj.metro.data.model.NavMenu
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor() {

    fun getNavMenus() = flow<List<NavMenu>> {
        val  menus = mutableListOf<NavMenu>()
        menus.add(NavMenu("检修工作流程",R.drawable.ic_overhaul_menu))
        menus.add(NavMenu("跟踪检修任务",R.drawable.ic_tracking_menu))
        menus.add(NavMenu("呼叫站区",R.drawable.ic_call_menu))
        menus.add(NavMenu("一键拍照录像",R.drawable.ic_camera_menu))
        menus.add(NavMenu("任务推送",R.drawable.ic_push_menu))
        menus.add(NavMenu("设置", R.drawable.ic_setting_menu))
        emit(menus)
    }
}