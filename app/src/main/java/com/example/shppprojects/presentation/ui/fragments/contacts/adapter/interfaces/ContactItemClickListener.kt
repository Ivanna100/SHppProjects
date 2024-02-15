package com.example.shppprojects.presentation.ui.fragments.contacts.adapter.interfaces

import android.view.View
import com.example.shppprojects.data.model.Contact

interface ContactItemClickListener {
    fun onClickDelete(contact: Contact)
    fun onClickContact(contact: Contact, transitionPairs: Array<Pair<View, String>>)
    fun onLongClick(contact: Contact)

}
