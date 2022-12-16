package com.jiemaibj.metro.data.model

import androidx.annotation.DrawableRes
import com.jiemaibj.metro.R


enum class NavMenu(
    val itemName: String,
    @DrawableRes val iconId: Int = R.drawable.ic_push_menu
) {
    OVERHAUL("检修工作流程",R.drawable.ic_overhaul_menu),
    TRACKING("跟踪检修任务",R.drawable.ic_tracking_menu),
    CALL("呼叫站区",R.drawable.ic_call_menu),
    CAMERA("一键拍照录像",R.drawable.ic_camera_menu),
    PUSH("任务推送",R.drawable.ic_push_menu),
    SETTINGS("设置", R.drawable.ic_setting_menu),
}
