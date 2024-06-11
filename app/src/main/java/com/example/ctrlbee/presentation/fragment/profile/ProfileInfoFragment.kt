package com.example.ctrlbee.presentation.fragment.profile

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ctrlbee.R
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentProfileInfoBinding
import com.example.ctrlbee.presentation.state.UpdateProfileState
import com.example.ctrlbee.presentation.viewmodel.ProfileInfoViewModel

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
private val MEDIA_TYPE_TEXT = "text/plain".toMediaType()
private val MEDIA_TYPE_IMAGE = "image/*".toMediaType()

@AndroidEntryPoint
class ProfileInfoFragment : Fragment(R.layout.fragment_profile_info) {

    @Inject lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val viewBinding: FragmentProfileInfoBinding by viewBinding()
    private val viewModel: ProfileInfoViewModel by viewModels()

    private lateinit var username: RequestBody

    private lateinit var profileImage: MultipartBody.Part
    private var filePath = ""
    private lateinit var file: File

    private lateinit var resultLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var permissionGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Dexter.withContext(requireActivity())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Log.i("DEBUG", "permission granted")
                    permissionGranted = true
                }
                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Log.i("DEBUG", "permission denied")
                }
                override fun onPermissionRationaleShouldBeShown(
                    p0: com.karumi.dexter.listener.PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).withErrorListener {
                Log.i("DEBUG", "error during requesting permission")
            }
            .onSameThread()
            .check()

        resultLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                val cursor = activity?.contentResolver?.query(
                    uri,
                    filePathColumn,
                    null,
                    null,
                    null
                )

                cursor?.moveToFirst()
                val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                filePath = columnIndex?.let { cursor.getString(it) }.toString()

                Glide.with(this)
                    .asBitmap()
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(viewBinding.imageViewButton)

                cursor?.close()
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initActions()
        initObserver()
    }

    private fun initActions() =
        with(viewBinding) {
//            setUpDropDownMenu()

            imageViewButton.setOnClickListener {
                openGallery()
            }

            buttonSave.setOnClickListener {
                username = fieldFullname.text.toString().toRequestBody(MEDIA_TYPE_TEXT)
//                dateOfBirthday =
//                    "${dropdownMenuYear.text}-${dropdownMenuMonth.text}-${dropdownMenuDay.text}".toRequestBody(
//                        MEDIA_TYPE_TEXT,
//                    )
//                country = dropdownMenuCountry.text.toString().toRequestBody(MEDIA_TYPE_TEXT)

                file = File(filePath)

                profileImage =
                    MultipartBody.Part.createFormData(
                        "profileImage",
                        file.name,
                        file.asRequestBody(MEDIA_TYPE_IMAGE),
                    )

                viewModel.updateProfile(
                    sharedPreferencesRepo.getUserAccessToken(),
                    username,
//                    dateOfBirthday,
//                    country,
                    profileImage,
                )
                sharedPreferencesRepo.setUsername(username.toString())
            }
        }


    private fun openGallery() {
        if (permissionGranted) {
            resultLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            Toast.makeText(requireContext(), "No permission..", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initObserver() {
        viewModel.profileStateLiveData.observe(viewLifecycleOwner, ::handleState)
    }

    private fun handleState(state: UpdateProfileState) {
        when (state) {
            is UpdateProfileState.Failed -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }

            is UpdateProfileState.Loading -> {}
            is UpdateProfileState.Success -> {
                if (state.message == "CONTINUE") {
                    Toast.makeText(
                        requireContext(),
                        "Profile Updated",
                        Toast.LENGTH_SHORT,
                    ).show()

                    sharedPreferencesRepo.apply {
                        setUsername(username.toString())
                        setUserImage(filePath)
                    }
                    val fragment = ProfileFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

}