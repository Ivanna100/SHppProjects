package com.example.shppprojects.presentation.ui.fragments.addcontacts

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.shppprojects.databinding.FragmentUsersBinding
import com.example.shppprojects.presentation.ui.fragments.BaseFragment
import com.example.shppprojects.presentation.ui.fragments.addcontacts.adapter.AddContactsAdapter
import com.example.shppprojects.presentation.ui.fragments.addcontacts.adapter.interfaces.UserItemClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactsFragment : BaseFragment<FragmentUsersBinding>(FragmentUsersBinding:: inflate) {

    private val args: AddContactsFragmentArgs by navArgs()
    private val viewModel: AddContactsViewModel by viewModels()
//    private  val adapter: AddContactsAdapter by lazy{
//        AddContactsAdapter(listener = object : UserItemClickListener{
//
//        })
//    }
}