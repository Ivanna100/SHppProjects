package com.example.shppprojects.presentation.ui.fragments.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.databinding.ItemUserBinding
import com.example.shppprojects.presentation.ui.fragments.contacts.adapter.utils.ContactDiffUtil
import com.example.shppprojects.presentation.utils.ext.gone
import com.example.shppprojects.presentation.utils.ext.loadImage

class SearchAdapter() :
    androidx.recyclerview.widget.ListAdapter<Contact, SearchAdapter.UsersViewHolder>(ContactDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class UsersViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            with(binding) {
                textViewName.text = contact.name
                textViewCareer.text = contact.career
                imageViewUserPhoto.loadImage(contact.photo)
                imageViewDelete.gone()
            }
        }
    }

}