package com.example.userregistration.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.userregistration.R
import com.example.userregistration.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), SwitchFragments {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.model = viewModel

    }

    override fun moveToRegister() {
        val navFragment = supportFragmentManager.findFragmentById(R.id.loginFragment)
        val navController = navFragment?.findNavController()
        val graph = navController?.graph
        graph?.setStartDestination(R.id.registerFragment)
        navController?.setGraph(graph!!, null)
    }

    override fun moveToLogIn() {
        val navController = findNavController(this.binding.fragContainer.id)
        val graph = navController.graph
        graph.setStartDestination(R.id.loginFragment)
        navController.setGraph(graph, null)
    }
}