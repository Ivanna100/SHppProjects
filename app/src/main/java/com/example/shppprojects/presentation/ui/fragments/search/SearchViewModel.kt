package com.example.shppprojects.presentation.ui.fragments.search

import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.data.database.DatabaseImpl
import com.example.shppprojects.data.model.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val notificationManagerCompat: NotificationManagerCompat,
    private val databaseImpl: DatabaseImpl,
) : ViewModel() {

    private val _currentList = MutableStateFlow<List<Contact>>(listOf())
    val currentList: StateFlow<List<Contact>> = _currentList

    private val startedListContact: ArrayList<Contact> = arrayListOf()

    fun initSearchList() = viewModelScope.launch(Dispatchers.IO) {
        _currentList.value = databaseImpl.getSearchList()
        startedListContact.clear()
        startedListContact.addAll(_currentList.value)
    }

    fun updateContactList(newText: String?): Int {
        val filteredList = startedListContact.filter { contact: Contact ->
            contact.name?.contains(newText ?: "", ignoreCase = true) == true
        }
        _currentList.value = filteredList
        return filteredList.size
    }

    fun cancelSimpleNotification() {
        notificationManagerCompat.cancel(1)
    }

}