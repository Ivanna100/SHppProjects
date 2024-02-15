package com.example.shppprojects.presentation.ui.fragments.auth.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.shppprojects.databinding.FragmentSignInBinding
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.utils.ext.gone
import com.example.shppprojects.presentation.utils.ext.showErrorSnackBar
import com.example.shppprojects.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel: SignInViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObserver()
    }

    private fun setListeners() {
        with(binding) {
            buttonLoginSignIn.setOnClickListener { signIn() }
            textViewSignUp.setOnClickListener { toSignUpScreen() }
        }
    }

    private fun signIn() {
        with(binding) {
            viewModel.authorizationUser(
                textInputEditTextEmailSignIn.text.toString(),
                textInputEditTextPasswordSignIn.text.toString()
            )
        }
    }

    private fun toSignUpScreen() {
        val direction = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
        navController.navigate(direction)
    }

    private fun setObserver() {
        with(binding) {
            lifecycleScope.launch {
                viewModel.authorizationState.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                    .collect {
                        when (it) {
                            is ApiStateUser.Success<*> -> {
                                if (checkboxRememberMeSignIn.isChecked) {
                                    viewModel.saveDataToDataStore(
                                            requireContext(),
                                            textInputEditTextEmailSignIn.text.toString(),
                                            textInputEditTextPasswordSignIn.text.toString()
                                        )
                                }
                                val direction =
                                    SignInFragmentDirections.actionSignInFragmentToViewPagerFragment()
                                navController.navigate(direction)
                            }

                            is ApiStateUser.Initial -> Unit

                            is ApiStateUser.Loading -> {
                                progressBar.visible()
                            }

                            is ApiStateUser.Error -> {
                                progressBar.gone()
                                root.showErrorSnackBar(requireContext(), it.error)
                            }
                        }
                    }
            }
        }
    }
}