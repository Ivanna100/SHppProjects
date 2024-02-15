package com.example.shppprojects.presentation.ui.fragments.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.shppprojects.R
import com.example.shppprojects.databinding.FragmentSplashBinding
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.utils.ext.invisible
import com.example.shppprojects.presentation.utils.ext.visible
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val viewModel: SplashViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isAutoLogin()
        setObserver()
    }

    private fun isAutoLogin() {
        lifecycleScope.launch {
            if (viewModel.isAutoLogin(requireContext())) {
                viewModel.autoLogin(requireContext())
            } else {
                val direction = SplashFragmentDirections.actionSplashFragmentToSignInFragment()
                navController.navigate(direction)
            }
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.authorizationState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    is ApiStateUser.Success<*> -> {
                        val direction =
                            SplashFragmentDirections.actionSplashFragmentToViewPagerFragment()
                        navController.navigate(direction)
                    }

                    is ApiStateUser.Loading -> {
                        binding.progressBar.visible()
                    }

                    is ApiStateUser.Initial -> Unit

                    is ApiStateUser.Error -> {
                        binding.progressBar.invisible()
                        Snackbar.make(
                            binding.root,
                            R.string.no_internet_connection,
                            Snackbar.LENGTH_INDEFINITE
                        ).setAction(R.string.repeat) {
                            viewModel.autoLogin(requireContext())
                        }.show()
                    }
                }
            }
        }
    }

}