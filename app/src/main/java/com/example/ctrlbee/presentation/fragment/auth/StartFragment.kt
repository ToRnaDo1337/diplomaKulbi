package com.example.ctrlbee.presentation.fragment.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.data.repository.SharedPreferencesRepo
import com.example.ctrlbee.databinding.FragmentStartBinding
import com.example.ctrlbee.presentation.state.LoginState
import com.example.ctrlbee.presentation.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment(R.layout.fragment_start) {
    @Inject
    lateinit var sharedPreferencesRepo: SharedPreferencesRepo
    private val viewBinding: FragmentStartBinding by viewBinding()
    private val viewModel: LoginViewModel by viewModels()

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
            buttonLogin.setOnClickListener {
                val email = fieldEmail.text.toString()
                val password = fieldPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Fill all the fields", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.signIn(email, password)
                }
            }
            buttonForgot.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_resetFragment)
            }
            buttonCreateAccount.setOnClickListener {
                findNavController().navigate(R.id.action_startFragment_to_registerFragment)
            }
        }

    private fun initObserver() {
        viewModel.loginStateLiveData.observe(viewLifecycleOwner, ::handleLoginState)
    }

    private fun handleLoginState(state: LoginState) {
        when (state) {
            is LoginState.Failed -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
            }
            is LoginState.Loading -> {}
            is LoginState.Success -> {
                val result = state.tokens
                sharedPreferencesRepo.setUserAccessToken(result.accessToken)
                sharedPreferencesRepo.setUserRefreshToken(result.refreshToken)
                findNavController().navigate(R.id.action_startFragment_to_homeActivity)
            }
        }
    }
}