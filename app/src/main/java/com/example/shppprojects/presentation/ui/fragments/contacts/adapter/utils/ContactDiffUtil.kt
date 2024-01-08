package com.example.shppprojects.presentation.ui.fragments.contacts.adapter.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.shppprojects.data.model.Contact


class ContactDiffUtil : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }
}