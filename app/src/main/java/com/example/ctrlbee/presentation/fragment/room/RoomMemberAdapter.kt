package com.example.ctrlbee.presentation.fragment.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ctrlbee.R  // Здесь замените "ctrlbee" на ваш пакет с ресурсами
import com.example.ctrlbee.domain.model.RoomMember
class RoomMemberAdapter(private val members: List<RoomMember>) : RecyclerView.Adapter<RoomMemberAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.username_text)
        private val profileImageView: ImageView = itemView.findViewById(R.id.profile_image)

        fun bind(member: RoomMember) {
            usernameTextView.text = member.username ?: "Unknown"
            // Здесь загрузите изображение профиля, используя member.profileImage
            // Glide.with(itemView.context).load(member.profileImage).into(profileImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room_member, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = members[position]
        holder.bind(member)
    }

    override fun getItemCount(): Int {
        return members.size
    }
}