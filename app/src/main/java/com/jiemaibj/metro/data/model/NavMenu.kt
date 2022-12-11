package com.jiemaibj.metro.data.model

import androidx.annotation.DrawableRes
import com.jiemaibj.metro.R


data class NavMenu(
    val name: String,
    @DrawableRes val iconId: Int = R.drawable.ic_push_menu
)
