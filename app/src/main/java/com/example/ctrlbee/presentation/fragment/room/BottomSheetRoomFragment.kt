package com.example.ctrlbee.presentation.fragment.room

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.ctrlbee.R
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentBottomSheetRoomBinding
import com.example.ctrlbee.domain.model.RoomRequest
import com.example.ctrlbee.presentation.state.RoomAddState
import com.example.ctrlbee.presentation.state.RoomState
import com.example.ctrlbee.presentation.viewmodel.RoomViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BottomSheetRoomFragment(private var expanded: Boolean = false): BottomSheetDialogFragment() {

    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private lateinit var binding: FragmentBottomSheetRoomBinding
    private val viewModel: RoomViewModel by viewModels()

    private fun initObservers() {
        viewModel.roomAddState.observe(viewLifecycleOwner, ::handleRoomAddState)
    }

    private fun handleRoomAddState(state: RoomAddState) = with(binding) {
        when (state) {
            is RoomAddState.Failed -> {
                Log.e("ToDoListFragmentTag", state.message)
                Toast.makeText(
                    requireContext(),
                    "Something went wrong!",
                    Toast.LENGTH_SHORT,
                ).show()
                dismiss()
            }
            is RoomAddState.Loading -> {}
            is RoomAddState.Success -> {
                val room = state.result
                Toast.makeText(
                    requireContext(),
                    "Room created successfully!",
                    Toast.LENGTH_SHORT,
                ).show()
                dismiss()
            }
        }
    }

    private lateinit var behavior: BottomSheetBehavior<FrameLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetRoomBinding.inflate(inflater, container, false)

        initObservers()

        binding.isRoomPrivate.setOnClickListener {
            binding.roomPasswordWrapper.visibility = if(binding.isRoomPrivate.isChecked) VISIBLE else INVISIBLE
        }

        binding.saveRoomBtn.setOnClickListener {

            if(binding.fieldRoomName.text.toString().isNotEmpty()){
                if(binding.isRoomPrivate.isChecked){
                    if(binding.fieldRoomPassword.text.toString().isNotEmpty()){
                        viewModel.savePrivateRoom(
                            token = sharedPreferencesRepo.getUserRefreshToken(),
                            password = binding.fieldRoomPassword.text.toString(),
                            room =
                            RoomRequest(
                                name = binding.fieldRoomName.text.toString(),
                                isPrivate = true,
                            )
                        )
                    }
                    else {
                        Toast.makeText(
                            requireContext(),
                            "Password can't be empty!",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                }
                else {
                    viewModel.saveRoom(
                        token = sharedPreferencesRepo.getUserRefreshToken(),
                        RoomRequest(
                            name = binding.fieldRoomName.text.toString(),
                            isPrivate = false,
                        )
                    )
                }
            }
            else {
                Toast.makeText(
                    requireContext(),
                    "Room name is empty!",
                    Toast.LENGTH_SHORT,
                ).show()
            }



        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        behavior = bottomSheet.behavior

        if (expanded) behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return bottomSheet
    }


}