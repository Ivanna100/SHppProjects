package com.example.shppprojects.presentation.ui.fragments.auth.signup.signupextended

import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.shppprojects.R
import com.example.shppprojects.data.model.UserRequest
import com.example.shppprojects.data.model.UserWithTokens
import com.example.shppprojects.databinding.SignUpExtendedBinding
import com.example.shppprojects.domain.state.UserApiResultState
import com.example.shppprojects.presentation.ui.fragments.BaseFragment
import com.example.shppprojects.utils.Constants
import com.example.shppprojects.utils.DataStore.saveData
import com.example.shppprojects.utils.Validation
import com.example.shppprojects.utils.ext.invisible
import com.example.shppprojects.utils.ext.loadImage
import com.example.shppprojects.utils.ext.log
import com.example.shppprojects.utils.ext.showErrorSnackBar
import com.example.shppprojects.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpExtendedFragment : BaseFragment<SignUpExtendedBinding>(SignUpExtendedBinding::inflate){

    private val viewModel: SignUpExtendedViewModel by viewModels()

    private val args : SignUpExtendedFragmentArgs by navArgs()

    private var photoUri: Uri? = null
    private val requestImageLauncher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
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
        cancel()
        forward()
        setPhoto()
        inputMobilePhone()
    }

    private fun cancel() {
        binding.buttonCancelSue.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun forward() {
        with(binding) {
            buttonForwardSue.setOnClickListener {
                if(!Validation.isValidUserName(textInputEditTextUserNameSue.text.toString())) {
                    root.showErrorSnackBar(requireContext(), R.string.user_name_must_contain_at_least_3_letters)
                } else if(!Validation.isValidMobilePhone(textInputEditTextMobilePhoneSue.text.toString())) {
                    root.showErrorSnackBar(requireContext(), R.string.phone_must_be_at_least_10_digits_long)
                } else {
                    log("user name extended register: " + textInputEditTextUserNameSue.text.toString())
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
    }

    private fun setPhoto() {
        binding.imageViewAddPhotoSue.setOnClickListener {
            val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            requestImageLauncher.launch(request)
        }
    }

    private fun inputMobilePhone() {
        binding.textInputEditTextMobilePhoneSue.addTextChangedListener(
            PhoneNumberFormattingTextWatcher(Constants.MOBILE_CODE)
        )
    }

    private fun setSignUpExtended() {
        binding.textInputEditTextUserNameSue.setText(parsingEmail(args.email))
    }

    private fun parsingEmail(email : String) : String {
        val elements = email.split("@")[0].replace(".", " ").split(" ")
        return if(elements.size >= 2) {
            "${elements[0].replaceFirstChar { it.uppercase() }} ${elements[1].replaceFirstChar { it.titlecase() }}"
        } else {
            elements[0]
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.registerState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect{
                when(it) {
                    is UserApiResultState.Success -> {
                        if(args.rememberMe) saveData(requireContext(), args.email, args.password)
                        viewModel.isLogout()
                        val direction = SignUpExtendedFragmentDirections.actionSignUpExtendedFragmentToViewPagerFragment(
                            UserWithTokens(
                                it.userData.user,
                                it.userData.accessToken,
                                it.userData.refreshToken
                            )
                        )
                        log("user after register on sign up extended fragment" + it.userData.user.toString())
                        navController.navigate(direction)
                    }

                    is UserApiResultState.Initial -> Unit

                    is UserApiResultState.Loading -> {
                        binding.progressBarSue.visible()
                    }

                    is UserApiResultState.Error -> {
                        binding.root.showErrorSnackBar(requireContext(), it.error)
                        viewModel.isLogout()
                        binding.progressBarSue.invisible()
                    }
                }
            }
        }
    }
}