package com.example.ctrlbee.presentation.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.core.Constants
import com.example.ctrlbee.databinding.FragmentChangePasswordBinding
import com.example.ctrlbee.databinding.FragmentResetBinding
import com.example.ctrlbee.presentation.state.ChangePasswordState
import com.example.ctrlbee.presentation.state.RegistrationState
import com.example.ctrlbee.presentation.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment : Fragment(R.layout.fragment_change_password) {

    private val viewModel: RegisterViewModel by viewModels()

    private val viewBinding: FragmentChangePasswordBinding by viewBinding()

    private lateinit var email: String
    private lateinit var verCode: String

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        email = requireArguments().getString(Constants.REG_USER_EMAIL)!!
        verCode = requireArguments().getString("ver_code")!!

        initActions()
        initObserver()
    }

    private fun initActions() =
        with(viewBinding) {

            buttonChange.setOnClickListener {
                val password1 = fieldPassword1.text.toString()
                val password2 = fieldPassword2.text.toString()

                if (password1.isNotEmpty() && password2.isNotEmpty()) {
                    if (password1 == password2) {
                        viewModel.changePassword(
                            email = email,
                            password = password1,
                            verificationCode = verCode
                        )
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Password are not the same!",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }

    private fun initObserver() {
        viewModel.changePasswordStateLiveData.observe(
            viewLifecycleOwner,
            ::handleChangePasswordState
        )
    }

    private fun handleChangePasswordState(state: ChangePasswordState) {
        when (state) {
            is ChangePasswordState.Failed -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_changePasswordFragment_to_startFragment)
            }

            is ChangePasswordState.Loading -> {}
            is ChangePasswordState.Success -> {

                Toast.makeText(
                    requireContext(),
                    "Password changed successfully!",
                    Toast.LENGTH_SHORT,
                ).show()
                findNavController().navigate(R.id.action_changePasswordFragment_to_startFragment)

            }
        }
    }

}