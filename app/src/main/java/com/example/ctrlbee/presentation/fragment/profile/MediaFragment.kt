package com.example.ctrlbee.presentation.fragment.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ctrlbee.R
import com.example.ctrlbee.databinding.FragmentProfileMediaBinding
import com.example.ctrlbee.domain.model.MediaItem
import androidx.fragment.app.FragmentManager
import by.kirich1409.viewbindingdelegate.fragmentViewBinding

class MediaFragment : Fragment(R.layout.fragment_profile_media) {

    private var _binding: FragmentProfileMediaBinding? = null
    private val binding get() = _binding

    private val mediaAdapter by lazy { MediaAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileMediaBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        binding?.recyclerView?.layoutManager = GridLayoutManager(requireContext(), 3)
        binding?.recyclerView?.adapter = mediaAdapter

        // Если у вас есть заранее подготовленные медиа-элементы
        val mediaItems = listOf(
            MediaItem("https://st.depositphotos.com/2935381/4189/i/450/depositphotos_41897159-stock-photo-example-concept.jpg", "Description 1"),
            MediaItem("https://st.depositphotos.com/2935381/4189/i/450/depositphotos_41897159-stock-photo-example-concept.jpg", "Description 2"),
            MediaItem("https://st.depositphotos.com/2935381/4189/i/450/depositphotos_41897159-stock-photo-example-concept.jpg", "Description 2"),
            MediaItem("https://img.freepik.com/premium-photo/wooden-cubes-with-word-example_284815-518.jpg", "Description 2")
        )

        mediaAdapter.submitList(mediaItems)

        mediaAdapter.setOnItemClickListener { mediaItem ->
            Log.d("MediaFragment", "onItemClick: mediaItem.image = ${mediaItem.image}")
            requireActivity().supportFragmentManager?.let {
                Log.d("MediaFragment", "showPhotoViewerDialog() called")
                showPhotoViewerDialog(it, mediaItem.image)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showPhotoViewerDialog(fragmentManager: FragmentManager, imageUrl: String) {
        Log.d("MediaFragment", "showPhotoViewerDialog() called with imageUrl: $imageUrl")
        val fragment = PhotoViewerDialogFragment.newInstance(imageUrl)
        fragment.show(fragmentManager, "photo_viewer_dialog")
    }

    fun addMediaItem(mediaItem: MediaItem) {
        val currentList = mediaAdapter.currentList.toMutableList()
        currentList.add(mediaItem)
        mediaAdapter.submitList(currentList)
    }
}
