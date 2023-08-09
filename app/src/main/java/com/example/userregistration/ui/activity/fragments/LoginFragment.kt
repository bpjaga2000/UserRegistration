package com.example.userregistration.ui.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userregistration.R
import com.example.userregistration.Utils.collectLatestFlow
import com.example.userregistration.databinding.FragmentLoginBinding
import com.example.userregistration.ui.activity.MainViewModel
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
        viewModel.resetModels()
        viewModel.toolBarText.set(getString(R.string.log_in))

        viewModel.userName.observe(viewLifecycleOwner) {
            viewModel.logInEnable()
        }

        viewModel.password.observe(viewLifecycleOwner) {
            viewModel.logInEnable()
        }

        collectLatestFlow(viewModel.logIn) {
            it?.let {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToLogoutFragment())
            } ?: Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show()
        }

        binding.register.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToLoginFragment())
        }

        binding.logIn.setOnClickListener {
            viewModel.logInUser()
        }
        return binding.root
    }
}