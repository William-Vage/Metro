package com.jiemaibj.metro.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jiemaibj.metro.data.model.NavMenu
import com.jiemaibj.metro.databinding.ItemMenuMainBinding

class MenuAdapter : ListAdapter<NavMenu, MenuAdapter.MenuViewHolder>(MenuDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ItemMenuMainBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MenuViewHolder(private val binding: ItemMenuMainBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: NavMenu) {
            with(binding) {
                binding.item = menu
                executePendingBindings()
            }
        }

        fun navigateToMenu() {
            //todo 跳转
        }
    }

    private class MenuDiffCallback : DiffUtil.ItemCallback<NavMenu>() {
        override fun areItemsTheSame(oldItem: NavMenu, newItem: NavMenu): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: NavMenu, newItem: NavMenu): Boolean {
            return oldItem == newItem
        }
    }
}