package com.example.ctrlbee.presentation.fragment.room

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ctrlbee.R
import com.example.ctrlbee.databinding.ItemRoomsBinding
import com.example.ctrlbee.domain.model.RoomAll

class RoomsAdapter(private val roomList: List<RoomAll>) : RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder>() {
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_rooms, parent, false)
        return RoomsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        val currentItem = roomList[position]
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem )
            }
        }
    }

    override fun getItemCount() = roomList.size

    class RoomsViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemRoomsBinding.bind(item)
        fun bind(room: RoomAll) = with(binding) {
            room1Name.text = room.name
            room1Memebers.text = "members: ${room.membersCount}"
            roomsLock.visibility = if(room.isPrivate) VISIBLE else INVISIBLE
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, room: RoomAll)
    }
}