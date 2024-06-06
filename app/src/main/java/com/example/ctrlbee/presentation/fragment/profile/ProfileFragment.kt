package com.example.ctrlbee.presentation.fragment.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import com.example.ctrlbee.presentation.fragment.profile.MediaAdapter

import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.ctrlbee.R
import android.net.Uri
import android.os.Environment
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentProfileBinding
import com.example.ctrlbee.domain.model.MediaItem
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val viewBinding: FragmentProfileBinding by viewBinding()

    private val pagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ProfilePagerAdapter(this)


    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101

    private val photos = mutableListOf<Bitmap>()

    private val tabTitles = listOf("Media", "Statistics")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initActions()

    }

    private fun initActions() = with(viewBinding) {
        profilePager.adapter = pagerAdapter
        TabLayoutMediator(profileTabs, profilePager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        Glide.with(viewBinding.root)
            .load("https://i.pinimg.com/236x/e6/8c/2b/e68c2bd8fa49f1b3400e2e152f2c2ef4.jpg")
            .circleCrop()
            .error(R.drawable.bee)
            .into(viewBinding.profileImage)

        iconMenu.setOnClickListener {
            showPopupMenu(it)
        }

        iconAddMedia.setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
        }

    }
    private fun checkCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let {
                checkSelfPermission(it, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            } ?: false
        } else {
            true
        }
    }
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {

            val imageBitmap = data?.extras?.get("data") as? Bitmap
//            imageBitmap?.let {
//                // Сохранение изображения во внутреннее хранилище
//                val savedImageUri = saveImageToInternalStorage(it)
//                savedImageUri?.let { uri ->
//                    // Добавление изображения в список медиафайлов
//                    val mediaItem = MediaItem(uri.toString(), "New Image")
//                    addMediaItem(mediaItem)
//                }
//            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.menuInflater.inflate(R.menu.items_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.option_2 -> {
                    sharedPreferencesRepo.clearAll()
                    activity?.finish()
                    true
                }

                R.id.option_1 -> {
                    val fragment = ProfileInfoFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
//                        .addToBackStack(null)
                        .commit()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        // Создание нового файла для сохранения изображения
        val imagesDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imagesDir, "profile_image.jpg")

        // Сохранение изображения на диск
        try {
            FileOutputStream(imageFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        // Возвращение URI сохраненного изображения
        return Uri.fromFile(imageFile)
    }

    // Метод для добавления медиафайла в список


}

