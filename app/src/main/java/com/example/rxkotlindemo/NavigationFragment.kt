package com.example.rxkotlindemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rxkotlindemo.databinding.FragmentNavigationBinding

class NavigationFragment :  Fragment() {

    private lateinit var binding: FragmentNavigationBinding

    private lateinit var navController : NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNavigationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewBinding()
    }

    private fun viewBinding() {
        binding.btnBaseClasses.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_baseClassesFragment)
        }

        binding.btnCreateOperators.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_createOperatorsFragment)
        }
        binding.btnFilterOperators.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_filterOperatorsFragment)
        }

        binding.btnTransformOperators.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_transformationOperatorsFragment)
        }

        binding.btnConcatenateOperators.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_combinationOperatorsFragment)
        }

        binding.btnHelperOperators.setOnClickListener {
            navController.navigate(R.id.action_mainFragment_to_helperOperatorsFragment)
        }
    }
}