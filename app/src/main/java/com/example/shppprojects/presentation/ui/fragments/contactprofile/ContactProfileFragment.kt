package com.example.shppprojects.presentation.ui.fragments.contactprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.shppprojects.R
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.databinding.FragmentDetailViewBinding
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.utils.Constants
import com.example.shppprojects.presentation.utils.ext.checkForInternet
import com.example.shppprojects.presentation.utils.ext.gone
import com.example.shppprojects.presentation.utils.ext.loadImage
import com.example.shppprojects.presentation.utils.ext.showErrorSnackBar
import com.example.shppprojects.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactProfileFragment : BaseFragment<FragmentDetailViewBinding>(
    FragmentDetailViewBinding::inflate
) {
    private val args: ContactProfileFragmentArgs by navArgs()
    private val viewModel: ContactProfileViewModel by viewModels()
    private val userData: ResponseOfUser.Data by lazy {
        viewModel.requestGetUser()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setObserver()
        setProfile(args.Contact)
        setSharedElementsTransition(args.Contact)
    }

    private fun setListeners() {
        navigationBack()
        addToContacts()
    }

    private fun navigationBack() {
        binding.imageViewNavigationBack.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun addToContacts() {
        binding.buttonMessage.setOnClickListener {
            if (args.isNewUser) {
                viewModel.addContact(
                    userData.user.id,
                    args.Contact,
                    userData.accessToken,
                    requireContext().checkForInternet()
                )
            }
        }
    }

    private fun setObserver() {
        with(binding) {
            lifecycleScope.launch {
                viewModel.usersState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                    when (it) {
                        is ApiStateUser.Initial -> Unit

                        is ApiStateUser.Loading -> {
                            progressBar.visible()
                        }

                        is ApiStateUser.Success<*> -> {
                            progressBar.gone()
                            buttonMessageTop.gone()
                            buttonMessage.text = getString(R.string.message)
                        }

                        is ApiStateUser.Error -> {
                            progressBar.gone()
                            root.showErrorSnackBar(requireContext(), it.error)
                            viewModel.changeState()
                        }
                    }
                }
            }
        }
    }

    private fun setProfile(contact: Contact) {
        with(binding) {
            if (args.isNewUser) {
                buttonMessageTop.visible()
                buttonMessage.text = getString(R.string.add_to_my_contacts)
            }
            textViewFullName.text = contact.name
            textViewCareer.text = contact.career
            textViewAddress.text = contact.address
            imageViewAvatar.loadImage(contact.photo)
        }
    }

    private fun setSharedElementsTransition(contact: Contact) {
        with(binding) {
            imageViewAvatar.transitionName = Constants.TRANSITION_NAME_IMAGE + contact.id
            textViewFullName.transitionName = Constants.TRANSITION_NAME_NAME + contact.id
            textViewCareer.transitionName = Constants.TRANSITION_NAME_CAREER + contact.id
        }
        val animation = android.transition.TransitionInflater.from(context).inflateTransition(
            R.transition.custom_move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

}