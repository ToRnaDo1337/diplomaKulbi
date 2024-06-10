package com.example.ctrlbee.presentation.fragment.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.core.Constants.Companion.REG_USER_EMAIL
import com.example.ctrlbee.core.Constants.Companion.REG_USER_PASSWORD
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentVerificationBinding
import com.example.ctrlbee.presentation.state.RegistrationState
import com.example.ctrlbee.presentation.state.VerificationState
import com.example.ctrlbee.presentation.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class VerificationFragment : Fragment(R.layout.fragment_verification) {
    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val viewBinding: FragmentVerificationBinding by viewBinding()
    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var email: String
    private var password: String? = null

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        email = requireArguments().getString(REG_USER_EMAIL)!!
        password = requireArguments().getString(REG_USER_PASSWORD)

        initActions()
        initObserver()
        initBack()

        viewModel.sendEmailVerification(
            email = email,
            smsRequestType = if (password != null) "REGISTER"
            else "FORGOT_PASSWORD",
        )
    }
    private fun initBack()= with(viewBinding){
        buttonBackRegister.setOnClickListener{
            findNavController().navigate(
                R.id.action_verificationFragment_to_startFragment
            )
        }
    }

    private fun initActions() =
        with(viewBinding) {
            buttonVerifyAccount.setOnClickListener {
                val verificationCode = fieldCode.text.toString()

                if (verificationCode.isNotEmpty()) {
                    //register
                    if (password != null) {
                        viewModel.signUp(
                            email = email,
                            password = password!!,
                            verificationCode = verificationCode,
                        )
                    }
                    //change password
                    else {
                        val bundle =
                            Bundle().apply {
                                putString(REG_USER_EMAIL, email)
                                putString("ver_code", verificationCode)
                            }
                        findNavController().navigate(
                            R.id.action_verificationFragment_to_changePasswordFragment,
                            bundle
                        )
                    }

                }
            }
        }

    private fun initObserver() {
        viewModel.registrationStateLiveData.observe(viewLifecycleOwner, ::handleRegistrationState)
        viewModel.verificationStateLiveData.observe(
            viewLifecycleOwner,
            ::handleSendingEmailVerification
        )
    }

    private fun handleSendingEmailVerification(state: VerificationState) {
        when (state) {
            is VerificationState.Failed -> {
                Toast.makeText(requireContext(), "Wrong type of email", Toast.LENGTH_LONG).show()
            }

            is VerificationState.Loading -> {
                // Optional: Show a progress indicator if needed
//                Toast.makeText(requireContext(), "Sending verification email...", Toast.LENGTH_SHORT).show()
            }

            is VerificationState.Success -> {
                Toast.makeText(requireContext(), "Verification email sent successfully", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun handleRegistrationState(state: RegistrationState) {
        when (state) {
            is RegistrationState.Failed -> {
                Toast.makeText(requireContext(), "Incorrect code", Toast.LENGTH_LONG).show()
            }

            is RegistrationState.Loading -> {
//                Toast.makeText(requireContext(), "Registration in progress...", Toast.LENGTH_SHORT).show()
            }

            is RegistrationState.Success -> {
                when (state.message) {
                    "CREATED" -> {
                        Toast.makeText(
                            requireContext(),
                            "Successful Registration",
                            Toast.LENGTH_SHORT,
                        ).show()

                        sharedPreferencesRepo.setUserEmail(email)
                        findNavController().navigate(R.id.action_verificationFragment_to_profileInfoFragment)
                    }
                    "ALREADY_REGISTERED" -> {
                        Toast.makeText(
                            requireContext(),
                            "User already registered",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    "INVALID_DETAILS" -> {
                        Toast.makeText(
                            requireContext(),
                            "Invalid registration details",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    else -> {
                        Toast.makeText(
                            requireContext(),
                            "Registration successful, but unknown response: ${state.message}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
            }
        }
    }

}
