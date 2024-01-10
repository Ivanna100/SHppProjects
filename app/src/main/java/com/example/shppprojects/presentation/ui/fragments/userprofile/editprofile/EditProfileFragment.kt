package com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.shppprojects.databinding.FragmentEditProfileBinding
import com.example.shppprojects.domain.state.UserApiResultState
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile.dialog.DialogCalendar
import com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile.interfaces.DialogCalendarListener
import com.example.shppprojects.utils.Constants
import com.example.shppprojects.utils.Parser
import com.example.shppprojects.utils.Validation
import com.example.shppprojects.utils.ext.invisible
import com.example.shppprojects.utils.ext.loadImage
import com.example.shppprojects.utils.ext.showErrorSnackBar
import com.example.shppprojects.utils.ext.visible
import com.example.shppprojects.utils.ext.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(FragmentEditProfileBinding::inflate){

    private val viewModel: EditProfileViewModel by viewModels()
    private val args: EditProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObserver()
        setInputs()
        setCalendar()
    }

    private fun setListeners() {
        save()
        inputPhone()
        navigationBack()
    }

    private fun save() {
        with(binding) {
            buttonSave.setOnClickListener {
                if(Validation.isValidUserName(textInputEditTextUserName.text.toString()) &&
                    Validation.isValidMobilePhone(textInputEditTextPhone.text.toString())) {
                    viewModel.editUser(
                        args.userData.user.id,
                        args.userData.accessToken,
                        textInputEditTextUserName.text.toString(),
                        textInputEditTextCareer.text.toString(),
                        textInputEditTextPhone.text.toString(),
                        textInputEditTextAddress.text.toString(),
                        Parser.getDataFromString(textInputEditTextDate.text.toString())
                    )
                }
            }
        }
    }

    private fun inputPhone() {
        binding.textInputEditTextPhone.addTextChangedListener {
            PhoneNumberFormattingTextWatcher(Constants.MOBILE_CODE)
        }
    }

    private fun navigationBack() {
        binding.imageViewNavigationBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.editUserState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect{
                when(it) {
                    is UserApiResultState.Success -> {
                        navController.navigateUp()
                    }

                    is UserApiResultState.Initial -> Unit

                    is UserApiResultState.Loading -> {
                        binding.progressBar.visible()
                    }

                    is UserApiResultState.Error -> {
                        binding.root.showErrorSnackBar(requireContext(), it.error)
                        binding.progressBar.invisible()
                    }
                }
            }
        }
    }

    private fun setInputs() {
        inputsErrors()
        with(binding) {
            imageViewSignUpExtendedMockup.invisible()
            imageViewSignUpExtendedPhoto.loadImage(args.userData.user.image)
            textInputEditTextUserName.setText(args.userData.user.name ?: "")
            textInputEditTextCareer.setText(args.userData.user.career ?: "")
            textInputEditTextPhone.setText(args.userData.user.phone ?: "")
            textInputEditTextAddress.setText(args.userData.user.address ?: "")
            textInputEditTextDate.setText(args.userData.user.birthday?.let {
                Parser.getStringFromData(it.toString()) } ?: "")
        }
    }

    private fun inputsErrors() {
        with(binding) {
            textInputEditTextUserName.doOnTextChanged { text, _, _, _ ->
                textViewInvalidUserName.visibleIf(!Validation.isValidUserName(text.toString()))
            }
            textInputEditTextPhone.doOnTextChanged { text, _, _, _ ->
                textViewInvalidPhone.visibleIf(!Validation.isValidMobilePhone(text.toString()))
            }
        }
    }

    private fun setCalendar() {
        with(binding) {
            textInputEditTextDate.setOnClickListener {
                val dialog = DialogCalendar()
                dialog.setListener(listener = object : DialogCalendarListener {
                    override fun onDateSelected(date: String) {
                        textInputEditTextDate.setText(date)
                    }
                })
                dialog.show(parentFragmentManager, Constants.DIALOG_TAG)
            }
        }
    }

}