package com.example.ctrlbee.presentation.fragment.room

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.core.Constants
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentRoomsInfoBinding
import com.example.ctrlbee.databinding.FragmentRoomsSearchBinding
import com.example.ctrlbee.domain.model.RoomAll
import com.example.ctrlbee.domain.model.RoomUno
import com.example.ctrlbee.presentation.state.RoomState
import com.example.ctrlbee.presentation.state.RoomUnoState
import com.example.ctrlbee.presentation.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class RoomsInfoFragment : Fragment(R.layout.fragment_rooms_info) {

    private lateinit var handler: Handler
    private var secondsElapsed = 0L
    private var isTimerRunning = false

    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val binding: FragmentRoomsInfoBinding by viewBinding()
    private val viewModel: RoomViewModel by viewModels()


    private fun initObservers() {
        viewModel.roomUnoState.observe(viewLifecycleOwner, ::handleRoomUnoState)
    }

    private fun initActions(room: RoomUno) = with(binding) {
        roomInfoRecyclerView.adapter = RoomInfoAdapter(room)
        roomInfoRecyclerView.layoutManager = LinearLayoutManager(context)

        val roomsAdapter = RoomInfoAdapter(room)

        roomInfoRecyclerView.adapter = roomsAdapter

        roomName.text = room.name
        roomMemberCount.text = "Members: ${room.members.size}"

        // Applying OnClickListener to our Adapter
        /*roomsAdapter.setOnClickListener(object : RoomsAdapter.OnClickListener {
            override fun onClick(position: Int, room: RoomAll) {
                Log.d("MyLog", "On Click Pressed")
                RoomsJoinFragment(room).show(childFragmentManager,"RoomsJoinFragment")
            }
        })*/
    }

    private fun handleRoomUnoState(state: RoomUnoState) = with(binding) {
        when (state) {
            is RoomUnoState.Failed -> {
                android.util.Log.e("ToDoListFragmentTag", state.message)
            }
            is RoomUnoState.Loading -> {}
            is RoomUnoState.Success -> {
                val room = state.result

                initActions(room)
            }
        }
    }

    private lateinit var roomId: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        roomId = requireArguments().getString("room_id").toString()

        handler = Handler(Looper.getMainLooper())

        initObservers()

        viewModel.fetchRoomById(
            token = sharedPreferencesRepo.getUserRefreshToken(),
            roomId = roomId
        )

        binding.backBtn.setOnClickListener {
            stopTimer()
            parentFragmentManager.popBackStack()
        }

        binding.pauseCircleBtn.setOnClickListener {
            if (isTimerRunning) {
                stopTimer()
            } else {
                startTimer()
            }
        }




    }

    private fun startTimer() {
        handler.post(object : Runnable {
            override fun run() {
                val hours = secondsElapsed / 3600
                val minutes = (secondsElapsed % 3600) / 60
                val seconds = secondsElapsed % 60

                binding.timerText.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                secondsElapsed++
                handler.postDelayed(this, 1000)
            }
        })

        isTimerRunning = true
        binding.pauseCircleBtn.setImageResource(R.drawable.pause_circle)
    }

    private fun stopTimer() {
        handler.removeCallbacksAndMessages(null)
        isTimerRunning = false
        binding.pauseCircleBtn.setImageResource(R.drawable.play_circle)
    }
    override fun onDestroyView() {
        super.onDestroyView()

        stopTimer()
    }
}


//package com.example.ctrlbee.presentation.fragment.room
//
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import by.kirich1409.viewbindingdelegate.viewBinding
//import com.example.ctrlbee.R
//import com.example.ctrlbee.core.Constants
//import com.example.ctrlbee.data.repository.SharedPreferencesRepo
//import com.example.ctrlbee.databinding.FragmentRoomsInfoBinding
//import com.example.ctrlbee.databinding.FragmentRoomsSearchBinding
//import com.example.ctrlbee.domain.model.RoomAll
//import com.example.ctrlbee.domain.model.RoomUno
//import com.example.ctrlbee.presentation.state.RoomState
//import com.example.ctrlbee.presentation.state.RoomUnoState
//import com.example.ctrlbee.presentation.viewmodel.RoomViewModel
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//
//@AndroidEntryPoint
//class RoomsInfoFragment : Fragment(R.layout.fragment_rooms_info) {
//    @Inject
//    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
//    private val binding: FragmentRoomsInfoBinding by viewBinding()
//    private val viewModel: RoomViewModel by viewModels()
//
//
//    private fun initObservers() {
//        viewModel.roomUnoState.observe(viewLifecycleOwner, ::handleRoomUnoState)
//    }
//
//    private fun initActions(room: RoomUno) = with(binding) {
//        roomInfoRecyclerView.adapter = RoomInfoAdapter(room)
//        roomInfoRecyclerView.layoutManager = LinearLayoutManager(context)
//
//        val roomsAdapter = RoomInfoAdapter(room)
//
//        roomInfoRecyclerView.adapter = roomsAdapter
//
//        roomName.text = room.name
//        roomMemberCount.text = "Members: ${room.members.size}"
//
//        // Applying OnClickListener to our Adapter
//        /*roomsAdapter.setOnClickListener(object : RoomsAdapter.OnClickListener {
//            override fun onClick(position: Int, room: RoomAll) {
//                Log.d("MyLog", "On Click Pressed")
//                RoomsJoinFragment(room).show(childFragmentManager,"RoomsJoinFragment")
//            }
//        })*/
//    }
//
//    private fun handleRoomUnoState(state: RoomUnoState) = with(binding) {
//        when (state) {
//            is RoomUnoState.Failed -> {
//                android.util.Log.e("ToDoListFragmentTag", state.message)
//            }
//            is RoomUnoState.Loading -> {}
//            is RoomUnoState.Success -> {
//                val room = state.result
//
//                initActions(room)
//            }
//        }
//    }
//
//    private lateinit var roomId: String
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        roomId = requireArguments().getString("room_id").toString()
//
//        initObservers()
//
//        viewModel.fetchRoomById(
//            token = sharedPreferencesRepo.getUserRefreshToken(),
//            roomId = roomId
//        )
//
//        binding.backBtn.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
//
//
//    }
//}