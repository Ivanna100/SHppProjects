package com.example.shppprojects.presentation.ui.fragments.userprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.shppprojects.data.userdataholder.UserDataHolder
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.databinding.FragmentProfileBinding
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.ui.fragments.viewpager.ViewPagerFragment
import com.example.shppprojects.presentation.ui.fragments.viewpager.ViewPagerFragmentDirections
import com.example.shppprojects.presentation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfile : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel: UserProfileViewModel by viewModels()
    private lateinit var userData: ResponseOfUser.Data

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialUser()
        setListeners()
        setUserProfile()
    }

    private fun initialUser() {
        userData = viewModel.getUser()
    }

    private fun setListeners() {
        with(binding) {
            buttonViewContacts.setOnClickListener { viewContacts() }
            buttonEditProfile.setOnClickListener { editProfile() }
            textViewLogout.setOnClickListener { logOut() }
        }
    }

    private fun viewContacts() {
        (parentFragment as? ViewPagerFragment)?.openFragment(Constants.CONTACTS_FRAGMENT)
    }

    private fun logOut() {
        viewModel.removeDataFromDataStore(requireContext())
        val direction = ViewPagerFragmentDirections.actionViewPagerFragmentToSignInFragment()
        navController.navigate(direction)
    }

    private fun editProfile() {
        val direction = ViewPagerFragmentDirections.actionViewPagerFragmentToEditProfileFragment()
        navController.navigate(direction)
    }

    private fun setUserProfile() {
        UserDataHolder.userData = viewModel.getUser()
        with(binding) {
            textViewName.text = userData.user.name
            textViewCareer.text = userData.user.career
            textViewHomeAddress.text = userData.user.address
        }
    }

}