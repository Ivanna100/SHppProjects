package com.example.shppprojects.presentation.ui.fragments.viewpager

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.shppprojects.databinding.FragmentViewPagerBinding
import com.example.shppprojects.presentation.ui.fragments.BaseFragment
import com.example.shppprojects.presentation.ui.fragments.viewpager.adapter.ViewPagerAdapter
import com.example.shppprojects.utils.Constants
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding>(FragmentViewPagerBinding::inflate) {

    private val args: ViewPagerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(this@ViewPagerFragment, args)
        with(binding) {
            viewPager.offscreenPageLimit = 1
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = when (position) {
                    Constants.FIRST_FRAGMENT -> "Profile"
                    Constants.SECOND_FRAGMENT -> "Contacts"
                    else -> throw IllegalStateException("Unknown tab!")
                }
            }.attach()
        }
    }

    fun openFragment(index: Int) {
        binding.viewPager.currentItem = index
    }
}