package com.jiemaibj.metro.ui.main

import androidx.lifecycle.ViewModel
import com.jiemaibj.metro.data.MainRepository
import com.jiemaibj.metro.data.model.NavMenu
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val navMenus: Flow<List<NavMenu>> = mainRepository.getNavMenus()
}