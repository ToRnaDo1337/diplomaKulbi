package com.example.ctrlbee.presentation.fragment.room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentRoomsBinding
import com.example.ctrlbee.databinding.FragmentRoomsSearchBinding
import com.example.ctrlbee.domain.model.RoomAll
import com.example.ctrlbee.presentation.state.RoomState
import com.example.ctrlbee.presentation.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomsSearchFragment : Fragment(R.layout.fragment_rooms_search) {
    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val binding: FragmentRoomsSearchBinding by viewBinding()
    private val viewModel: RoomViewModel by viewModels()


    private fun initObservers() {
        viewModel.roomSearchState.observe(viewLifecycleOwner, ::handleRoomListState)
    }

    private fun initActions(rooms: List<RoomAll>) = with(binding) {
        roomsSearchRecyclerView.adapter = RoomsAdapter(rooms)
        roomsSearchRecyclerView.layoutManager = LinearLayoutManager(context)

        val roomsAdapter = RoomsAdapter(rooms)

        roomsSearchRecyclerView.adapter = roomsAdapter

        // Applying OnClickListener to our Adapter
        //(roomsSearchRecyclerView.adapter as RoomsAdapter)
        roomsAdapter.setOnClickListener(object : RoomsAdapter.OnClickListener {
            override fun onClick(position: Int, room: RoomAll) {
                Log.d("MyLog", "On Click Pressed")
                RoomsJoinFragment(room).show(childFragmentManager,"RoomsJoinFragment")
            }
        })
    }

    private fun handleRoomListState(state: RoomState) = with(binding) {
        when (state) {
            is RoomState.Failed -> {
                android.util.Log.e("ToDoListFragmentTag", state.message)
            }
            is RoomState.Loading -> {}
            is RoomState.Success -> {
                val rooms = state.result

                initActions(rooms)
            }
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        viewModel.fetchAllRoomsSearch(
            token = sharedPreferencesRepo.getUserRefreshToken(),
            roomName = ""
        )

        binding.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
//        binding.searchBtn.isInvisible
////        binding.searchBtn.setOnClickListener {
////            Log.d("MyLog", binding.fieldSearch.toString())
////            viewModel.fetchAllRoomsSearch(
////                token = sharedPreferencesRepo.getUserRefreshToken(),
////                roomName = binding.fieldSearch.text.toString()
////            )
////        }



    }
}