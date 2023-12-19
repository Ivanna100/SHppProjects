package com.example.shppprojects.presentation.ui.fragments.auth.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.shppprojects.data.model.UserRequest
import com.example.shppprojects.data.model.UserWithTokens
import com.example.shppprojects.databinding.FragmentSignInBinding
import com.example.shppprojects.domain.state.UserApiResultState
import com.example.shppprojects.presentation.ui.fragments.BaseFragment
import com.example.shppprojects.utils.DataStore.saveData
import com.example.shppprojects.utils.ext.gone
import com.example.shppprojects.utils.ext.log
import com.example.shppprojects.utils.ext.showErrorSnackBar
import com.example.shppprojects.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel : SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObserver()
    }

    private fun setListeners() {
        logIn()
        signUp()
    }

    private fun logIn() {
        with(binding) {
            buttonLoginSignIn.setOnClickListener{
                viewModel.authorizationUser(
                    UserRequest(
                        textInputEditTextEmailSignIn.text.toString(),
                        textInputEditTextPasswordSignIn.text.toString()
                    )
                )
            }
        }
    }

    private fun signUp() {
        binding.textViewSignUpSignIn.setOnClickListener {
            val direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            navController.navigate(direction)
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.authorizationState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect{
                when(it) {
                    is UserApiResultState.Success -> {
                        log("login success")
                        if(binding.checkboxRememberMeSignIn.isChecked) {
                            lifecycleScope.launch(Dispatchers.IO) {
                                saveData(
                                    requireContext(),
                                    binding.textInputEditTextEmailSignIn.text.toString(),
                                    binding.textInputEditTextPasswordSignIn.text.toString()
                                )
                            }
                        }
                        val direction = SignInFragmentDirections.actionSignInFragmentToViewPagerFragment(
                            UserWithTokens(
                                it.userData.user,
                                it.userData.accessToken,
                                it.userData.refreshToken
                            )
                        )
                        log("navigate sign In to View Pager")
                        navController.navigate(direction)
                    }

                    is UserApiResultState.Initial -> Unit

                    is UserApiResultState.Loading -> {
                        binding.progressBar.visible()
                    }

                    is UserApiResultState.Error -> {
                        binding.progressBar.gone()
                        binding.root.showErrorSnackBar(requireContext(), it.error)
                    }
                }
            }
        }
    }
}