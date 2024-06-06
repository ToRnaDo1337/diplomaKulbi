package com.example.ctrlbee.presentation.fragment.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.databinding.FragmentProfileMediaBinding
import com.example.ctrlbee.domain.model.MediaItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MediaFragment : Fragment(R.layout.fragment_profile_media) {

    private val viewBinding: FragmentProfileMediaBinding by viewBinding()

    private val mediaAdapter by lazy(LazyThreadSafetyMode.NONE) {
        MediaAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActions()
    }

    private fun initActions() = with(viewBinding) {
        mediaRecycler.adapter = mediaAdapter
        mediaRecycler.layoutManager = GridLayoutManager(context, 3)

//        val testList = listOf(
//            MediaItem("https://images.unsplash.com/photo-1554080353-a576cf803bda?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "1"),
//            MediaItem("https://images.unsplash.com/photo-1508921912186-1d1a45ebb3c1?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "2"),
//            MediaItem("https://images.unsplash.com/photo-1531804055935-76f44d7c3621?q=80&w=1888&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "3"),
//            MediaItem("https://images.unsplash.com/photo-1566275529824-cca6d008f3da?q=80&w=1887&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "4"),
//            MediaItem("https://images.unsplash.com/photo-1495231916356-a86217efff12?q=80&w=1936&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "5")
//        )


//        mediaAdapter.submitList(testList)
    }
//    private fun addMediaItem(mediaItem: MediaItem) {
//        // Добавление медиафайла в список
//        // После добавления уведомление адаптера о изменениях
//        mediaAdapter.add(MediaItem)
//        mediaAdapter.notifyDataSetChanged()
//    }

}