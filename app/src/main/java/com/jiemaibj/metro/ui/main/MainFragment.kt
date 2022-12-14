package com.jiemaibj.metro.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.jiemaibj.metro.databinding.FragmentMainBinding
import com.jiemaibj.metro.utilities.launchAndCollectIn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAdapter = MenuAdapter()
        with(binding.recycler) {
            adapter = mAdapter
        }
        subscribeUi(mAdapter)
    }

    private fun subscribeUi(menuAdapter: MenuAdapter) {
        mainViewModel.navMenus.launchAndCollectIn(this, Lifecycle.State.STARTED) {
            Log.i("lolo", "subscribeUi: $it")
            menuAdapter.submitList(it)
        }
    }
}
