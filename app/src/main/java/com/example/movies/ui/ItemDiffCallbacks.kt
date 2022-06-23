package com.example.movies.ui

import androidx.recyclerview.widget.DiffUtil

class ItemDiffCallbacks : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

    override fun getChangePayload(oldItem: Item, newItem: Item): Any? {
        return oldItem.getChangePayload(newItem)
    }

}