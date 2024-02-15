package com.example.shppprojects.presentation.ui.fragments.addcontacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.databinding.FragmentUsersBinding
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.ui.fragments.addcontacts.adapter.AddContactsAdapter
import com.example.shppprojects.presentation.ui.fragments.addcontacts.adapter.interfaces.UserItemClickListener
import com.example.shppprojects.presentation.utils.ext.checkForInternet
import com.example.shppprojects.presentation.utils.ext.invisible
import com.example.shppprojects.presentation.utils.ext.log
import com.example.shppprojects.presentation.utils.ext.showErrorSnackBar
import com.example.shppprojects.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactsFragment : BaseFragment<FragmentUsersBinding>(FragmentUsersBinding::inflate) {

    private val viewModel: AddContactsViewModel by viewModels()
    private val userData: ResponseOfUser.Data by lazy {
        viewModel.requestGetUser()
    }

    private val adapter: AddContactsAdapter by lazy {
        AddContactsAdapter(listener = object : UserItemClickListener {
            override fun onClickAdd(contact: Contact) {
                viewModel.addContact(
                    userData.user.id,
                    contact,
                    userData.accessToken,
                    requireContext().checkForInternet()
                )
            }

            override fun onClickContact(
                contact: Contact,
                transitionPairs: Array<Pair<View, String>>,
            ) {
                val extras = FragmentNavigatorExtras(*transitionPairs)
                val direction =
                    AddContactsFragmentDirections.actionAddContactsFragmentToContactProfileFragment(
                        !viewModel.supportList.contains(contact),
                        contact
                    )
                navController.navigate(direction, extras)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialRecyclerView()
        setObservers()
        setListeners()
    }

    private fun initialRecyclerView() {
        log("userData.accessToken:  ${userData.accessToken}")
        viewModel.getAllUsers(
            userData.accessToken,
            userData.user,
            requireContext().checkForInternet()
        )
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewUsers.adapter = adapter
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.users.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                adapter.submitList(it)
            }
        }

        with(binding) {
            lifecycleScope.launch {
                viewModel.usersState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                    when (it) {
                        is ApiStateUser.Success<*> -> {
                            progressBar.invisible()
                        }

                        is ApiStateUser.Loading -> {
                            progressBar.visible()
                        }

                        is ApiStateUser.Initial -> {
                            progressBar.invisible()
                        }

                        is ApiStateUser.Error -> {
                            progressBar.invisible()
                            root.showErrorSnackBar(requireContext(), it.error)
                            viewModel.changeState()
                        }
                    }
                }
            }

        }

        lifecycleScope.launch {
            viewModel.states.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                adapter.setStates(it)
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            imageSearchView.setOnClickListener { searchView() }
            imageViewNavigationBack.setOnClickListener { navigateBack() }
        }
    }

    private fun navigateBack() {
        navController.navigateUp()
    }

    private fun searchView() {
        viewModel.showNotification(requireContext())
    }

}