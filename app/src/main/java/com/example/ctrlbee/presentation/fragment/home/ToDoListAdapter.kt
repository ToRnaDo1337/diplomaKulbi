package com.example.ctrlbee.presentation.fragment.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ctrlbee.databinding.ItemToDoBinding
import com.example.ctrlbee.domain.model.ToDoListItem

class ToDoListAdapter : ListAdapter<ToDoListItem, ToDoListAdapter.ToDoListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ToDoListViewHolder {
        val itemBinding = ItemToDoBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return ToDoListViewHolder(itemBinding)
    }

    override fun onBindViewHolder(
        holder: ToDoListViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ToDoListItem>() {
            override fun areItemsTheSame(oldItem: ToDoListItem, newItem: ToDoListItem): Boolean {
                return oldItem.message == newItem.message
            }
            override fun areContentsTheSame(oldItem: ToDoListItem, newItem: ToDoListItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class ToDoListViewHolder(
        private val itemBinding: ItemToDoBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(toDoListItem: ToDoListItem) = with(itemBinding) {
            todoText.text = toDoListItem.message
        }
    }
}
