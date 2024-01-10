package com.example.shppprojects.presentation.ui.fragments.addcontacts

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.UserWithTokens
import com.example.shppprojects.databinding.FragmentUsersBinding
import com.example.shppprojects.domain.state.ArrayDataApiResultState
import com.example.shppprojects.presentation.ui.base.BaseFragment
import com.example.shppprojects.presentation.ui.fragments.addcontacts.adapter.AddContactsAdapter
import com.example.shppprojects.presentation.ui.fragments.addcontacts.adapter.interfaces.UserItemClickListener
import com.example.shppprojects.utils.ext.invisible
import com.example.shppprojects.utils.ext.showErrorSnackBar
import com.example.shppprojects.utils.ext.visible
import com.example.shppprojects.utils.ext.visibleIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactsFragment : BaseFragment<FragmentUsersBinding>(FragmentUsersBinding:: inflate) {

    private val args: AddContactsFragmentArgs by navArgs()
    private val viewModel: AddContactsViewModel by viewModels()
    private  val adapter: AddContactsAdapter by lazy{
        AddContactsAdapter(listener = object : UserItemClickListener{
            override fun onClickAdd(contact: Contact) {
                viewModel.addContact(args.userData.user.id, contact, args.userData.accessToken)
            }

            override fun onClickContact(
                contact: Contact,
                transitionPairs: Array<Pair<View, String>>
            ) {
                val extras = FragmentNavigatorExtras(*transitionPairs)
                val direction =
                    AddContactsFragmentDirections.actionAddContactsFragmentToContactProfileFragment(
                        !viewModel.supportList.contains(contact),
                        UserWithTokens(
                            args.userData.user,
                            args.userData.accessToken,
                            args.userData.refreshToken
                        ), contact
                )
//                closeSearchView()
                navController.navigate(direction, extras)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllUsers(args.userData.accessToken, args.userData.user)
        initialRecyclerView()
        setObservers()
        setListeners()
    }

    private fun initialRecyclerView() {
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewUsers.adapter = adapter
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.users.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect{
                adapter.submitList(it)
            }
        }

        lifecycleScope.launch {
            viewModel.usersState.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect{
                when(it) {
                    is ArrayDataApiResultState.Success -> {
                        binding.progressBar.invisible()
                    }

                    is ArrayDataApiResultState.Loading -> {
                        binding.progressBar.visible()
                    }

                    is ArrayDataApiResultState.Initial -> Unit

                    is ArrayDataApiResultState.Error -> {
                        binding.progressBar.invisible()
                        binding.root.showErrorSnackBar(requireContext(), it.error)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.states.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect{
                adapter.setStates(it)
            }
        }
    }

    private fun setListeners() {
        with(binding) {
            textViewUsers.setOnClickListener { searchView() }
            imageSearchView.setOnClickListener { searchView() }
            imageViewNavigationBack.setOnClickListener { navigateBack() }
        }

    }

    private fun navigateBack() {
            navController.navigateUp()
    }

    private fun searchView() {
        viewModel.showNotification(requireContext())
//        with(binding) {
//            imageSearchView.setOnCloseListener {
//                imageSearchView.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
//                textViewUsers.visible()
//                imageViewNavigationBack.visible()
//                false
//            }
//            imageSearchView.setOnSearchClickListener {
//                imageSearchView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//                textViewUsers.invisible()
//                imageViewNavigationBack.invisible()
//            }
//            imageSearchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    textViewNoResultFound.visibleIf(viewModel.updateContactList(newText) == 0)
//                    if(newText.isNullOrEmpty()) initialRecyclerView()
//                    return false
//                }
//            } )
//        }
    }

    private fun closeSearchView() {
//        with(binding) {
//            imageSearchView.setQuery("", false)
//            imageSearchView.isIconified= true
//        }
    }
}