package com.example.shppprojects.presentation.ui.fragments.addcontacts.adapter.interfaces

import android.view.View
import com.example.shppprojects.data.model.Contact

interface UserItemClickListener {
    fun onClickAdd(contact: Contact)
    fun onClickContact(contact: Contact, transitionPairs : Array<Pair<View, String>>)
}