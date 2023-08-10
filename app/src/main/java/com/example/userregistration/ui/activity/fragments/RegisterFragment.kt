package com.example.userregistration.ui.activity.fragments

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userregistration.R
import com.example.userregistration.databinding.FragmentRegisterBinding
import com.example.userregistration.ui.activity.MainViewModel
import com.example.userregistration.utils.InputFilters
import com.example.userregistration.utils.collectLatestFlow
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
        viewModel.getRegionsFromDb()
        viewModel.toolBarText.set(getString(R.string.register_sign_up))
        viewModel.registerEnable()

        viewModel.userName.observe(viewLifecycleOwner) {
            viewModel.registerEnable()
        }

        binding.passwordTil.errorIconDrawable = null

        viewModel.password.observe(viewLifecycleOwner) {
            viewModel.registerEnable()
            if (!it.isNullOrBlank() && viewModel.passwordError() > 0)
                binding.passwordTil.error = getString(viewModel.passwordError())
            else
                binding.passwordTil.error = null
        }

        viewModel.region.observe(viewLifecycleOwner) {
            viewModel.registerEnable()
        }

        binding.region.onItemClickListener =
            OnItemClickListener { _, _, position, _ -> viewModel.region.value = position }

        collectLatestFlow(viewModel.countries) { res ->
            res?.let {
                if (it.isEmpty()) {
                    viewModel.getRegion()
                    viewModel.progressBoolean.set(true)
                } else {
                    viewModel.addToDb()
                    binding.region.setAdapter(
                        ArrayAdapter(
                            requireContext(),
                            R.layout.item_dropdown_layout,
                            R.id.dropdownTv,
                            it.map { c -> c.name })
                    )
                    viewModel.progressBoolean.set(false)
                }
            }
        }

        binding.userName.filters =
            arrayOf(InputFilters.usernameFilter, InputFilter.LengthFilter(32))
        binding.password.filters =
            arrayOf(InputFilters.passwordFilter, InputFilter.LengthFilter(32))

        collectLatestFlow(viewModel.logIn) {
            if (findNavController().currentDestination?.id == R.id.registerFragment)
                it?.let { findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLogoutFragment()) }
        }

        collectLatestFlow(viewModel.checkUser) {
            it?.let {
                if (it <= 0) viewModel.addUser()
                else Toast.makeText(
                    requireContext(),
                    "User already exists!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.logIn.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.registerFragment)
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }

        binding.register.setOnClickListener {
            viewModel.checkUserName()
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetModels()
        binding.region.clearListSelection()
        binding.region.clearComposingText()
        binding.region.text = null
    }
}