package com.example.shppprojects.presentation.ui.fragments.auth.signup.signupextended

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.shppprojects.R
import com.example.shppprojects.data.model.UserRequest
import com.example.shppprojects.databinding.SignUpExtendedBinding
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.utils.Parser
import com.example.shppprojects.presentation.utils.Validation
import com.example.shppprojects.presentation.utils.ext.invisible
import com.example.shppprojects.presentation.utils.ext.loadImage
import com.example.shppprojects.presentation.utils.ext.showErrorSnackBar
import com.example.shppprojects.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpExtendedFragment : BaseFragment<SignUpExtendedBinding>(SignUpExtendedBinding::inflate) {

    private val viewModel: SignUpExtendedViewModel by viewModels()

    private val args: SignUpExtendedFragmentArgs by navArgs()

    private var photoUri: Uri? = null
    private val requestImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                photoUri = it
                binding.imageViewSignUpPhotoSue.loadImage(it.toString())
                binding.imageViewSignUpMockupSue.visibility = View.GONE
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setSignUpExtended()
        setObservers()
    }

    private fun setListeners() {
        with(binding) {
            buttonForwardSue.setOnClickListener { forward() }
            buttonCancelSue.setOnClickListener { cancel() }
            imageViewAddPhotoSue.setOnClickListener { setPhoto() }
        }
    }

    private fun cancel() {
        navController.navigateUp()
    }

    private fun forward() {
        with(binding) {
            if (!Validation.isValidUserName(textInputEditTextUserNameSue.text.toString())) {
                root.showErrorSnackBar(
                    requireContext(),
                    R.string.user_name_must_contain_at_least_3_letters
                )
            } else if (!Validation.isValidMobilePhone(textInputEditTextMobilePhoneSue.text.toString())) {
                root.showErrorSnackBar(
                    requireContext(),
                    R.string.phone_must_be_at_least_10_digits_long
                )
            } else {
                viewModel.registerUser(
                    UserRequest(
                        args.email,
                        args.password,
                        textInputEditTextUserNameSue.text.toString(),
                        textInputEditTextMobilePhoneSue.text.toString()
                    )
                )

            }
        }
    }

    private fun setPhoto() {
        val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        requestImageLauncher.launch(request)
    }

    private fun setSignUpExtended() {
        binding.textInputEditTextUserNameSue.setText(Parser.parsingEmail(args.email))
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.registerState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    is ApiStateUser.Success<*> -> {
                        if (args.rememberMe) viewModel.saveUserDataToDataStore(
                            requireContext(),
                            args.email,
                            args.password
                        )
                        viewModel.isLogout()
                        val direction =
                            SignUpExtendedFragmentDirections.actionSignUpExtendedFragmentToViewPagerFragment()
                        navController.navigate(direction)
                    }

                    is ApiStateUser.Initial -> Unit

                    is ApiStateUser.Loading -> {
                        binding.progressBarSue.visible()
                    }

                    is ApiStateUser.Error -> {
                        binding.root.showErrorSnackBar(requireContext(), it.error)
                        viewModel.isLogout()
                        binding.progressBarSue.invisible()
                    }
                }
            }
        }
    }
}