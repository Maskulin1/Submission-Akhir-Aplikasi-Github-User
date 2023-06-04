package com.reihan.githubuserapp.ui.main

import androidx.recyclerview.widget.DiffUtil
import com.reihan.githubuserapp.data.response.ItemsItem

class MainDiffCallback(
    private val oldList: List<ItemsItem>,
    private val newList: List<ItemsItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}