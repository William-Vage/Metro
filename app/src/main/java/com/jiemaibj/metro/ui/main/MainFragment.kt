package com.jiemaibj.metro.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jiemaibj.metro.R
import com.jiemaibj.metro.data.model.NavMenu
import com.jiemaibj.metro.databinding.FragmentMainBinding
import com.jiemaibj.metro.utilities.SM3Util.hash
import com.jiemaibj.metro.utilities.launchAndCollectIn
import dagger.hilt.android.AndroidEntryPoint
import org.bouncycastle.util.encoders.Hex
import java.security.MessageDigest
import kotlin.system.measureNanoTime


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
        val mAdapter = MenuAdapter {
            Log.i("main", it.toString())
            when (it) {
                NavMenu.PUSH -> {
                    findNavController().navigate(R.id.action_mainFragment_to_taskFragment)
                }
                NavMenu.CALL -> {
                    val plainText = "原文信息"
                    val hash: ByteArray
                    val timeCost1 = measureNanoTime {
                        hash = hash(plainText.toByteArray())
                    }
                    var result1 = "$timeCost1"
                    var sm3 = Hex.toHexString(hash)
                    val digest: MessageDigest = MessageDigest.getInstance("SHA-256")
                    val timeCost2 = measureNanoTime {
                        digest.update(plainText.toByteArray())
                        digest.digest()
                    }
                    var result2 = "$timeCost2"
                    var sha = Hex.toHexString(digest.digest())
                    var result = sm3 + "/n " + sha + "/n " + result1 + "/n " + result2
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Message")
                        .setMessage(result)
                        .show()
                }
                else -> Unit
            }
        }
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
