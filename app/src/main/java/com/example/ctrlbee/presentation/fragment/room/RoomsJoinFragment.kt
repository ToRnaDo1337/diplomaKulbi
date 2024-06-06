package com.example.ctrlbee.presentation.fragment.room

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.dialogFragmentViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentBottomSheetRoomBinding
import com.example.ctrlbee.databinding.FragmentRegisterBinding
import com.example.ctrlbee.databinding.FragmentRoomsJoinBinding
import com.example.ctrlbee.databinding.FragmentRoomsSearchBinding
import com.example.ctrlbee.domain.model.RoomAll
import com.example.ctrlbee.domain.model.RoomRequest
import com.example.ctrlbee.presentation.state.RoomAddState
import com.example.ctrlbee.presentation.state.RoomJoinState
import com.example.ctrlbee.presentation.viewmodel.RoomViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomsJoinFragment(
    private val room: RoomAll
) : DialogFragment() {

    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private lateinit var binding: FragmentRoomsJoinBinding
    private val viewModel: RoomViewModel by viewModels()

    private fun initObservers() {
        viewModel.roomJoinState.observe(viewLifecycleOwner, ::handleRoomJoinState)
    }

    private fun initActions(room: RoomAll) = with(binding) {

    }

    private fun handleRoomJoinState(state: RoomJoinState) = with(binding) {
        when (state) {
            is RoomJoinState.Failed -> {
                Log.e("ToDoListFragmentTag", state.message)
                Toast.makeText(
                    requireContext(),
                    "Something went wrong!",
                    Toast.LENGTH_SHORT,
                ).show()
                dismiss()
            }
            is RoomJoinState.Loading -> {}
            is RoomJoinState.Success -> {
                Toast.makeText(
                    requireContext(),
                    "You joined successfully!",
                    Toast.LENGTH_SHORT,
                ).show()
                dismiss()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomsJoinBinding.inflate(inflater, container, false)

        initObservers()

        Log.d("MyLog", "room name: ${room.name}" )
        //initActions(myRoom)
        binding.roomName.text = room.name
        binding.roomMemberCount.text = "Members: ${room.membersCount}"
        binding.isPrivate.text = if(room.isPrivate) "Private Group" else "Public Group"
        binding.fieldRoomPassword.visibility = if(room.isPrivate) VISIBLE else GONE
        binding.roomPasswordWrapper.visibility = if(room.isPrivate) VISIBLE else GONE

        binding.closeBtn.setOnClickListener { dismiss() }

        binding.roomJoinBtn.setOnClickListener {
            val password = binding.fieldRoomPassword.text.toString()
            if(room.isPrivate){
                if(password.isNotEmpty()){
                    viewModel.joinPrivateRoom(
                        token = sharedPreferencesRepo.getUserRefreshToken(),
                        roomId = room.id,
                        password = password
                    )
                }
                else {
                    Toast.makeText(
                        requireContext(),
                        "Password is empty!",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
            else {
                viewModel.joinRoom(
                    token = sharedPreferencesRepo.getUserRefreshToken(),
                    roomId = room.id
                )
            }

        }

        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val dialogBinding = layoutInflater.inflate(R.layout.fragment_rooms_join, null)
        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }
}