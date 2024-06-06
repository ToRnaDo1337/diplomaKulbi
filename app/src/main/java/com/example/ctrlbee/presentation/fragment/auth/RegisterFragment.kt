package com.example.ctrlbee.presentation.fragment.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.ctrlbee.R
import com.example.ctrlbee.core.Constants.Companion.REG_USER_EMAIL
import com.example.ctrlbee.core.Constants.Companion.REG_USER_PASSWORD
import com.example.ctrlbee.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val viewBinding: FragmentRegisterBinding by viewBinding()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initActions()
    }

    private fun initActions() =
        with(viewBinding) {
            buttonLogin.setOnClickListener {
                findNavController().navigate(R.id.action_registerFragment_to_startFragment)
            }

            buttonCreateAccount.setOnClickListener {
                val username = fieldName.text.toString()
                val password = fieldPassword.text.toString()
                val email = fieldEmail.text.toString()

                if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
                    val bundle =
                        Bundle().apply {
                            putString(REG_USER_EMAIL, email)
                            putString(REG_USER_PASSWORD, password)
                        }
                    findNavController().navigate(R.id.action_registerFragment_to_verificationFragment, bundle)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }

}
