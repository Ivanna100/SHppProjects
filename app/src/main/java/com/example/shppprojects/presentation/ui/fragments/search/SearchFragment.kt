package com.example.shppprojects.presentation.ui.fragments.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shppprojects.databinding.FragmentSearchBinding
import com.example.shppprojects.presentation.ui.base.BaseFragment

class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageViewNavigationBack.setOnClickListener { navController.navigateUp() }
    }
}