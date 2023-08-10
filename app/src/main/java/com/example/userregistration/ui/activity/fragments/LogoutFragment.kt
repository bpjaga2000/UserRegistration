package com.example.userregistration.ui.activity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.userregistration.R
import com.example.userregistration.databinding.FragmentLogoutBinding
import com.example.userregistration.ui.activity.MainViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class LogoutFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModel()
    private lateinit var binding: FragmentLogoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentLogoutBinding.inflate(inflater, container, false)
        binding.model = viewModel
        viewModel.toolBarText.set(getString(R.string.log_out))
        viewModel.isEnabled.set(true)
        binding.logOut.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetModels()
    }
}