package com.example.userregistration.ui.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userregistration.R
import com.example.userregistration.Utils.collectLatestFlow
import com.example.userregistration.databinding.FragmentRegisterBinding
import com.example.userregistration.ui.activity.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class RegisterFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModel()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.model = viewModel
        viewModel.resetModels()
        viewModel.getRegionsFromDb()
        viewModel.toolBarText.set(getString(R.string.register_sign_up))
        viewModel.registerEnable()

        viewModel.userName.observe(viewLifecycleOwner) {
            viewModel.registerEnable()
        }

        viewModel.password.observe(viewLifecycleOwner) {
            viewModel.registerEnable()
        }

        viewModel.region.observe(viewLifecycleOwner) {
            viewModel.registerEnable()
        }

        collectLatestFlow(viewModel.countries) {
            if (it.isEmpty())
                viewModel.getRegion()
            else {
                binding.region.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.fragment_register,
                        it.map { c -> c.name })
                )
            }
        }

        collectLatestFlow(viewModel.logIn) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLogoutFragment())
        }

        collectLatestFlow(viewModel.checkUser) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLogoutFragment())
        }
        binding.logIn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionLoginFragmentToLoginFragment())
        }

        binding.register.setOnClickListener {
            viewModel.checkUserName()
        }
        return binding.root
    }
}