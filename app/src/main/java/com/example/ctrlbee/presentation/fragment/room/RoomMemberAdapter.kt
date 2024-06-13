package com.example.ctrlbee.presentation.fragment.room

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ctrlbee.R  // Здесь замените "ctrlbee" на ваш пакет с ресурсами
import com.example.ctrlbee.domain.model.RoomMember
class RoomMemberAdapter(private val members: List<RoomMember>) : RecyclerView.Adapter<RoomMemberAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val usernameTextView: TextView = itemView.findViewById(R.id.username_text)
        private val profileImageView: ImageView = itemView.findViewById(R.id.profile_image)

        fun bind(member: RoomMember) {
            usernameTextView.text = member.username ?: "Unknown"
            // Here we assume member.profileImage is a Base64 encoded string
            val profImageString = member.profileImage

            try {
                if (profImageString != null && profImageString.isNotEmpty()) {
                    // Decode the Base64 string to a byte array
                    val profImageBytes = Base64.decode(profImageString, Base64.DEFAULT)

                    // Convert the byte array to a Bitmap
                    val bitmap = BitmapFactory.decodeByteArray(profImageBytes, 0, profImageBytes.size)

                    // Load the Bitmap into the ImageView using Glide
                    Glide.with(itemView.context)
                        .load(bitmap)
                        .circleCrop()
                        .into(profileImageView)

                } else {
                    // Handle the case where profImageString is null or empty
                    Glide.with(itemView.context)
                        .load(R.drawable.vector_prof) // Placeholder image if string is null or empty
                        .into(profileImageView)
                }
            } catch (e: Exception) {
                Log.e("ROOM MEMBER IMAGE EX", e.message.toString())
                // Handle exception appropriately, maybe load a placeholder image
                Glide.with(itemView.context)
                    .load(R.drawable.vector_prof) // Error image
                    .into(profileImageView)
            }

            itemView.setOnClickListener {
                showProfilePopup(member)
            }

        }
        private fun showProfilePopup(member: RoomMember) {
            val dialogView = LayoutInflater.from(itemView.context).inflate(R.layout.dialog_profile_popup, null)
            val profileImageViewPopup: ImageView = dialogView.findViewById(R.id.profileImageViewPopup)
            val usernameTextViewPopup: TextView = dialogView.findViewById(R.id.usernameTextViewPopup)

            usernameTextViewPopup.text = member.username ?: "Unknown"

            val profImageString = member.profileImage
            try {
                if (profImageString != null && profImageString.isNotEmpty()) {
                    val profImageBytes = Base64.decode(profImageString, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(profImageBytes, 0, profImageBytes.size)
                    Glide.with(itemView.context)
                        .load(bitmap)
                        .circleCrop()
                        .into(profileImageViewPopup)
                } else {
                    Glide.with(itemView.context)
                        .load(R.drawable.vector_prof) // Placeholder image
                        .circleCrop()
                        .into(profileImageViewPopup)
                }
            } catch (e: Exception) {
                Log.e("ROOM MEMBER IMAGE EX", e.message.toString())
                Glide.with(itemView.context)
                    .load(R.drawable.vector_prof) // Error image
                    .circleCrop()
                    .into(profileImageViewPopup)
            }

            AlertDialog.Builder(itemView.context)
                .setView(dialogView)
                .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                .show()
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