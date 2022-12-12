package com.jiemaibj.metro.data

import com.jiemaibj.metro.data.model.NavMenu
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor() {

    fun getNavMenus() = flow<List<NavMenu>> {
        emit(NavMenu.values().toMutableList())
    }
}