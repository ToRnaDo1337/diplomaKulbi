package com.example.ctrlbee.presentation.fragment.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ctrlbee.databinding.ItemMediaBinding
import com.example.ctrlbee.domain.model.MediaItem

class MediaAdapter : ListAdapter<MediaItem, MediaAdapter.MediaViewHolder>(DIFF_CALLBACK) {

    private var onItemClickListener: ((MediaItem) -> Unit)? = null
    fun setOnItemClickListener(listener: (MediaItem) -> Unit) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaViewHolder {
        val itemBinding = ItemMediaBinding.inflate(
            LayoutInflater.from(parent.context), parent,
            false
        )
        return MediaViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(currentItem)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MediaItem>() {
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem.image == newItem.image &&
                        oldItem.description == newItem.description
            }
            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MediaViewHolder(
        val itemBinding: ItemMediaBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(mediaItem: MediaItem) = with(itemBinding) {
            Glide.with(itemBinding.root)
                .load(mediaItem.image)
                .centerCrop()
                .into(media)
        }
    }


}