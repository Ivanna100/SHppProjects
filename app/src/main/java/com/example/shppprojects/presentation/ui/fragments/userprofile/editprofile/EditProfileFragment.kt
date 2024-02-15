package com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.databinding.FragmentEditProfileBinding
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile.dialog.DialogCalendar
import com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile.interfaces.DialogCalendarListener
import com.example.shppprojects.presentation.utils.Constants
import com.example.shppprojects.presentation.utils.Parser
import com.example.shppprojects.presentation.utils.Validation
import com.example.shppprojects.presentation.utils.ext.checkForInternet
import com.example.shppprojects.presentation.utils.ext.invisible
import com.example.shppprojects.presentation.utils.ext.loadImage
import com.example.shppprojects.presentation.utils.ext.showErrorSnackBar
import com.example.shppprojects.presentation.utils.ext.visible
import com.example.shppprojects.presentation.utils.ext.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(
    FragmentEditProfileBinding::inflate
) {

    private val viewModel: EditProfileViewModel by viewModels()

    private val userData: ResponseOfUser.Data by lazy {
        viewModel.getUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setObserver()
        setInputs()
        setCalendar()
    }

    private fun setListeners() {
        with(binding) {
            buttonSave.setOnClickListener { save() }
            imageViewNavigationBack.setOnClickListener { navigationBack() }
        }
    }

    private fun save() {
        with(binding) {
            if (Validation.isValidUserName(textInputEditTextUserName.text.toString()) &&
                Validation.isValidMobilePhone(textInputEditTextPhone.text.toString())
            ) {
                viewModel.editUser(
                    userData.user.id,
                    userData.accessToken,
                    textInputEditTextUserName.text.toString(),
                    textInputEditTextCareer.text.toString(),
                    textInputEditTextPhone.text.toString(),
                    textInputEditTextAddress.text.toString(),
                    Parser.getDataFromString(textInputEditTextDate.text.toString()),
                    userData.refreshToken,
                    requireContext().checkForInternet()
                )
            }
        }
    }

    private fun navigationBack() {
        navController.navigateUp()
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.editUserState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                when (it) {
                    is ApiStateUser.Success<*> -> {
                        navController.navigateUp()
                    }

                    is ApiStateUser.Initial -> Unit

                    is ApiStateUser.Loading -> {
                        binding.progressBar.visible()
                    }

                    is ApiStateUser.Error -> {
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
            imageViewSignUpExtendedPhoto.loadImage(userData.user.image)
            textInputEditTextUserName.setText(userData.user.name ?: "")
            textInputEditTextCareer.setText(userData.user.career ?: "")
            textInputEditTextPhone.setText(userData.user.phone ?: "")
            textInputEditTextAddress.setText(userData.user.address ?: "")
            textInputEditTextDate.setText(userData.user.birthday?.let {
                Parser.getStringFromData(it.toString())
            } ?: "")
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