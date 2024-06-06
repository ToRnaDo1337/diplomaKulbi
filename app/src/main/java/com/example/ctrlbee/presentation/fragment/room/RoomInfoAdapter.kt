package com.example.ctrlbee.presentation.fragment.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ctrlbee.R
import com.example.ctrlbee.databinding.ItemRoomMemberBinding
import com.example.ctrlbee.domain.model.RoomMember
import com.example.ctrlbee.domain.model.RoomUno

class RoomInfoAdapter(private val room: RoomUno) : RecyclerView.Adapter<RoomInfoAdapter.RoomsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_room_member, parent, false)
        return RoomsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        val currentItem = room.members[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = room.members.size

    class RoomsViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemRoomMemberBinding.bind(item)
        fun bind(member: RoomMember) = with(binding) {
            memberName.text = member.username
            Glide.with(memberAvatar.context).load(member.profileImage).into(memberAvatar)
        }
    }
}