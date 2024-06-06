package com.example.ctrlbee.presentation.fragment.room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.core.Constants
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentProfileBinding
import com.example.ctrlbee.databinding.FragmentRoomsBinding
import com.example.ctrlbee.domain.model.RoomAll
import com.example.ctrlbee.presentation.fragment.home.ToDoListFragment
import com.example.ctrlbee.presentation.state.RoomState
import com.example.ctrlbee.presentation.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomsFragment : Fragment(R.layout.fragment_rooms) {

    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val binding: FragmentRoomsBinding by viewBinding()
    private val viewModel: RoomViewModel by viewModels()

    private val continentDialog = BottomSheetRoomFragment(true)

    private fun initObservers() {
        viewModel.roomState.observe(viewLifecycleOwner, ::handleRoomListState)
    }

    private fun initActions(rooms: List<RoomAll>) = with(binding) {
        roomsRecyclerView.adapter = RoomsAdapter(rooms)
        roomsRecyclerView.layoutManager = LinearLayoutManager(context)

        val roomsAdapter = RoomsAdapter(rooms)

        roomsRecyclerView.adapter = roomsAdapter

        // Applying OnClickListener to our Adapter
        roomsAdapter.setOnClickListener(object : RoomsAdapter.OnClickListener {
            override fun onClick(position: Int, room: RoomAll) {
                val fragment = RoomsInfoFragment()

                fragment.arguments = Bundle().apply {
                    putString("room_id", room.id)
                }

                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }

    private fun handleRoomListState(state: RoomState) = with(binding) {
        when (state) {
            is RoomState.Failed -> {
                Log.e("ToDoListFragmentTag", state.message)
            }
            is RoomState.Loading -> {}
            is RoomState.Success -> {
                val rooms = state.result
                initActions(rooms)
            }
        }
    }

    /*override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRoomsBinding.inflate(layoutInflater)
        return binding.root
    }*/



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        viewModel.fetchAllRooms(
            token = sharedPreferencesRepo.getUserRefreshToken(),
            roomName = ""
        )

        binding.floatingActionButton.setOnClickListener{
            continentDialog.show(childFragmentManager,"BottomSheetFragment")
        }

        binding.searchBtn.setOnClickListener{
            val fragment = RoomsSearchFragment()

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }


//        binding.cardviewButton.setOnClickListener {
//            childFragmentManager.beginTransaction().apply {
//
//            }
//        }

    }
}