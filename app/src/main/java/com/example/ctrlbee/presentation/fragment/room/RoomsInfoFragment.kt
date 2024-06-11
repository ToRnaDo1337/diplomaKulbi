package com.example.ctrlbee.presentation.fragment.room
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentRoomsInfoBinding
import com.example.ctrlbee.domain.model.RoomUno
import com.example.ctrlbee.presentation.state.RoomUnoState
import com.example.ctrlbee.presentation.viewmodel.RoomViewModel
import com.example.ctrlbee.presentation.viewmodel.SharedTimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RoomsInfoFragment : Fragment(R.layout.fragment_rooms_info) {

    private val viewModel: RoomViewModel by viewModels()
    private val timerViewModel: SharedTimerViewModel by activityViewModels()

    private val binding: FragmentRoomsInfoBinding by viewBinding()

    @Inject // Добавим внедрение зависимости для sharedPreferencesRepo
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roomId = requireArguments().getString("room_id").toString()

        setupObservers()

        viewModel.fetchRoomById(
            token = sharedPreferencesRepo.getUserRefreshToken(),
            roomId = roomId
        )

        // Установка изображения кнопки при создании фрагмента
        if (timerViewModel.isTimerRunning) {
            binding.pauseCircleBtn.setImageResource(R.drawable.pause_circle)
        } else {
            binding.pauseCircleBtn.setImageResource(R.drawable.play_circle)
        }

        binding.backBtn.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.pauseCircleBtn.setOnClickListener {
            if (timerViewModel.isTimerRunning) {
                timerViewModel.pauseTimer()
                binding.pauseCircleBtn.setImageResource(R.drawable.play_circle)
            } else {
                timerViewModel.startTimer()
                binding.pauseCircleBtn.setImageResource(R.drawable.pause_circle)
            }
        }
    }

    private fun setupObservers() {
        viewModel.roomUnoState.observe(viewLifecycleOwner, ::handleRoomUnoState)
        timerViewModel.secondsElapsed.observe(viewLifecycleOwner) { seconds ->
            val hours = seconds / 3600
            val minutes = (seconds % 3600) / 60
            val seconds = seconds % 60
            binding.timerText.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

    private fun handleRoomUnoState(state: RoomUnoState) {
        when (state) {
            is RoomUnoState.Success -> {
                val room = state.result
                initRoomInfo(room)
            }
            is RoomUnoState.Failed -> {
                Log.e("RoomsInfoFragment", state.message)
            }
            is RoomUnoState.Loading -> {
                // Handle loading state if needed
            }

        }
    }

    private fun initRoomInfo(room: RoomUno) {
        with(binding) {
            roomInfoRecyclerView.layoutManager = LinearLayoutManager(context)
            roomInfoRecyclerView.adapter = RoomInfoAdapter(room)
            roomName.text = room.name
            roomMemberCount.text = "Members: ${room.members.size}"
        }
    }
}
