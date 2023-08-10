package com.example.userregistration.ui.activity.fragments

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userregistration.R
import com.example.userregistration.databinding.FragmentLoginBinding
import com.example.userregistration.ui.activity.MainViewModel
import com.example.userregistration.utils.InputFilters
import com.example.userregistration.utils.collectLatestFlow
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LoginFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModel()
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.model = viewModel
        viewModel.toolBarText.set(getString(R.string.log_in))

        viewModel.userName.observe(viewLifecycleOwner) {
            viewModel.logInEnable()
        }

        viewModel.password.observe(viewLifecycleOwner) {
            viewModel.logInEnable()
        }

        collectLatestFlow(viewModel.logIn) {
            it?.let {
                if (it.userName.isBlank())
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.invalid_credentials), Toast.LENGTH_SHORT
                    ).show()
                else if (findNavController().currentDestination?.id == R.id.loginFragment)
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToLogoutFragment())
            }
        }

        with(binding) {
            register.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.loginFragment)
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }

            logIn.setOnClickListener {
                viewModel.logInUser()
            }

            userName.filters = arrayOf(InputFilters.usernameFilter, InputFilter.LengthFilter(32))
            password.filters = arrayOf(InputFilters.passwordFilter, InputFilter.LengthFilter(32))

            return binding.root
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetModels()
    }
}