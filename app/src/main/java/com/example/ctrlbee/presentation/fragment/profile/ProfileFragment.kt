package com.example.ctrlbee.presentation.fragment.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.ctrlbee.R
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentProfileBinding
import com.example.ctrlbee.domain.model.MediaItem
import com.example.ctrlbee.domain.model.profile.ProfileResponse
import com.example.ctrlbee.presentation.viewmodel.ProfileInfoViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val viewBinding: FragmentProfileBinding by viewBinding()

    private val viewModel: ProfileInfoViewModel by viewModels()

    private val pagerAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ProfilePagerAdapter(this)
    }

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val CAMERA_REQUEST_CODE = 101

    private val tabTitles = listOf("Media", "Statistics")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserProfileData()
        initActions()
    }

    private fun initActions() = with(viewBinding) {
        profilePager.adapter = pagerAdapter
        TabLayoutMediator(profileTabs, profilePager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        iconMenu.setOnClickListener {
            showPopupMenu(it)
        }

        iconAddMedia.setOnClickListener {
            if (checkCameraPermission()) {
                openCamera()
            } else {
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            }
        }




        bioText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                sharedPreferencesRepo.setUserBio(bioText.text.toString())
                val statusRequestBody =
                    bioText.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
                viewModel.updateStatus(
                    sharedPreferencesRepo.getUserAccessToken(),
                    statusRequestBody
                )
                bioText.clearFocus()
                true
            } else {
                false
            }
        }

    }

    private fun initUserProfileData() = with(viewBinding) {

        viewModel.fetchProfile(sharedPreferencesRepo.getUserAccessToken())
        val username = sharedPreferencesRepo.getUsername();
        val bio = sharedPreferencesRepo.getUserBio();

        val profileImage = sharedPreferencesRepo.getUserImage();


//        Log.d("PROFILE FRAGMENT","USERIMAGE:"+profileImage)
        if (username != null && bio != null) {
            usernameText.text = username;
            bioText.setText(bio);
        }
        if (profileImage != null && profileImage != "NO_VALUE") {
            // Decode the Base64 string to a ByteArray
            try {
                val imageBytes = Base64.decode(profileImage, Base64.DEFAULT)

                // Convert the ByteArray to Bitmap
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                // Load the Bitmap into the ImageView using Glide
                Glide.with(viewBinding.root)
                    .load(bitmap)
                    .circleCrop()
                    .error(R.drawable.bee)
                    .into(viewBinding.profileImage)
            } catch (e: Exception) {
                e.message?.let { Log.d("PROFILE EXCEPTION", it) }
            }

        } else {
            Glide.with(viewBinding.root)
                .load("https://i.pinimg.com/236x/e6/8c/2b/e68c2bd8fa49f1b3400e2e152f2c2ef4.jpg")
                .circleCrop()
                .error(R.drawable.bee)
                .into(viewBinding.profileImage)
        }

    }

    private fun checkCameraPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let {
                checkSelfPermission(
                    it,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
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
            imageBitmap?.let {
                val savedImageUri = saveImageToInternalStorage(it)
                savedImageUri?.let { uri ->
                    val mediaItem = MediaItem(uri.toString(), "New Image")
                    addMediaItemToMediaFragment(mediaItem)
                }
            }
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
                        .commit()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val imagesDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File(imagesDir, "profile_image_${System.currentTimeMillis()}.jpg")

        try {
            FileOutputStream(imageFile).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return Uri.fromFile(imageFile)
    }

    private fun addMediaItemToMediaFragment(mediaItem: MediaItem) {
        val mediaFragment = childFragmentManager.findFragmentByTag("f0") as? MediaFragment
        mediaFragment?.let {
            it.addMediaItem(mediaItem)
        } ?: run {
            // Если MediaFragment еще не добавлен, попробуйте добавить его снова или выполнить другие необходимые действия.
        }
    }
}
