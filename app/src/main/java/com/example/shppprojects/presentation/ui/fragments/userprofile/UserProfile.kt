package com.example.shppprojects.presentation.ui.fragments.userprofile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.shppprojects.data.model.UserData
import com.example.shppprojects.data.model.UserWithTokens
import com.example.shppprojects.databinding.FragmentProfileBinding
import com.example.shppprojects.domain.state.UserApiResultState
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.ui.fragments.viewpager.ViewPagerFragment
import com.example.shppprojects.presentation.ui.fragments.viewpager.ViewPagerFragmentDirections
import com.example.shppprojects.utils.Constants
import com.example.shppprojects.utils.DataStore
import com.example.shppprojects.utils.ext.gone
import com.example.shppprojects.utils.ext.showErrorSnackBar
import com.example.shppprojects.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserProfile : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate){

    private val args: UserProfileArgs by navArgs()
    private val viewModel : UserProfileViewModel by viewModels()
    private lateinit var user : UserData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialUser()
        setListeners()
        setUserProfile()
        setObserver()
    }

    private fun initialUser() {
        viewModel.getUser(args.userData.user.id, args.userData.accessToken)
        user = args.userData.user
    }

    private fun setListeners() {
        viewContacts()
        logOut()
        editProfile()
    }

    private fun viewContacts() {
        binding.buttonViewContacts.setOnClickListener{
            (parentFragment as? ViewPagerFragment)?.openFragment(Constants.SECOND_FRAGMENT)
        }
    }

    private fun logOut() {
        binding.textViewLogout.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                DataStore.deleteDataFromDataStore(requireContext(), Constants.KEY_EMAIL)
                DataStore.deleteDataFromDataStore(requireContext(), Constants.KEY_PASSWORD)
                DataStore.deleteDataFromDataStore(requireContext(), Constants.KEY_REMEMBER_ME)
            }
            val direction = ViewPagerFragmentDirections.actionViewPagerFragmentToSignInFragment()
            navController.navigate(direction)
        }
    }

    private fun editProfile() {
        binding.buttonEditProfile.setOnClickListener {
            val direction = ViewPagerFragmentDirections.actionViewPagerFragmentToEditProfileFragment(
                UserWithTokens(
                    user,
                    args.userData.accessToken,
                    args.userData.refreshToken
                )
            )
            navController.navigate(direction)
        }
    }

    private fun setUserProfile() {
        with(binding) {
            textViewName.text = user.name
            textViewCareer.text = user.career
            textViewHomeAddress.text = user.address
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.getUserState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect{
                when(it) {
                    is UserApiResultState.Success -> {
                        with(binding) {
                            textViewCareer.visible()
                            textViewHomeAddress.visible()
                            progressBar.gone()
                        }
                        user = it.userData.user
                        setUserProfile()
                    }

                    is UserApiResultState.Initial -> Unit

                    is UserApiResultState.Loading -> {
                        binding.progressBar.visible()
                    }

                    is UserApiResultState.Error -> {
                        binding.root.showErrorSnackBar(requireContext(), it.error)
                    }
                }
            }
        }
    }

}