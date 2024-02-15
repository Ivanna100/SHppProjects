package com.example.shppprojects.presentation.ui.fragments.contacts

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.R
import com.example.shppprojects.data.userdataholder.UserDataHolder
import com.example.shppprojects.data.database.DatabaseImpl
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.data.repository.ContactsRepositoryImpl
import com.example.shppprojects.domain.state.ApiStateUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val contactsRepositoryImpl: ContactsRepositoryImpl,
    private val databaseImpl: DatabaseImpl,
    private val notificationManager: NotificationManagerCompat,
    private val notificationBuilder: NotificationCompat.Builder,
) : ViewModel() {

    private val _usersStateFlow = MutableStateFlow<ApiStateUser>(ApiStateUser.Initial)
    val usersState: StateFlow<ApiStateUser> = _usersStateFlow

    private val _contactList = MutableStateFlow(listOf<Contact>())
    val contactList: StateFlow<List<Contact>> = _contactList

    private val _selectContacts = MutableStateFlow<List<Contact>>(listOf())
    val selectContacts: StateFlow<List<Contact>> = _selectContacts

    private val _isMultiselect = MutableStateFlow(false)
    val isMultiselect = _isMultiselect


    private val _isSelectItem: MutableStateFlow<ArrayList<Pair<Boolean, Int>>> =
        MutableStateFlow(ArrayList())
    val isSelectItem: StateFlow<ArrayList<Pair<Boolean, Int>>> = _isSelectItem

    fun initialContactList(userId: Long, accessToken: String, hasInternet: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            _usersStateFlow.value = ApiStateUser.Loading
            _usersStateFlow.value = if (hasInternet) {
                contactsRepositoryImpl.getUserContacts(userId, accessToken)
            } else {
                databaseImpl.getContacts()
            }
            _contactList.value = UserDataHolder.serverContacts
            databaseImpl.addUsersToSearchList(_contactList.value)
        }

    private fun addContact(userId: Long, contact: Contact, accessToken: String) =
        viewModelScope.launch(Dispatchers.IO) {
            _usersStateFlow.value = ApiStateUser.Loading
            _usersStateFlow.value = contactsRepositoryImpl.addContact(userId, accessToken, contact)
            databaseImpl.addToSearchList(contact)
        }

    fun addContactToList(
        userId: Long,
        contact: Contact,
        accessToken: String,
        position: Int = _contactList.value.size,
    ): Boolean {
        val contactList = _contactList.value.toMutableList()
        if (!contactList.contains(contact)) {
            contactList.add(position, contact)
            _contactList.value = contactList
            addContact(userId, contact, accessToken)
            return true
        }
        return false
    }

    fun addSelectContact(contact: Contact): Boolean {
        val contactList = _selectContacts.value.toMutableList()
        if (!contactList.contains(contact)) {
            contactList.add(contact)
            _selectContacts.value = contactList
            _isSelectItem.value.add(Pair(true, contact.id.toInt()))
            return true
        }
        return false
    }

    private fun deleteContact(userId: Long, accessToken: String, contact: Contact) =
        viewModelScope.launch(Dispatchers.IO) {
            _usersStateFlow.value =
                contactsRepositoryImpl.deleteContact(userId, accessToken, contact)
            databaseImpl.deleteFromSearchList(contact)
        }

    fun deleteContactFromList(
        userId: Long,
        accessToken: String,
        contact: Contact,
        hasInternet: Boolean,
    ): Boolean {
        val contactList = _contactList.value.toMutableList()

        if (!hasInternet) {
            _usersStateFlow.value = ApiStateUser.Error(R.string.no_internet_connection)
            return false
        }

        if (contactList.contains(contact)) {
            deleteContact(userId, accessToken, contact)
            contactList.remove(contact)
            _contactList.value = contactList
            return true
        }
        return false
    }

    private fun deleteSelectContact(contact: Contact): Boolean {
        val contactList = _selectContacts.value.toMutableList()
        if (contactList.contains(contact)) {
            contactList.remove(contact)
            _selectContacts.value = contactList
            val id = contact.id.toInt()
            val index = _isSelectItem.value.indexOfFirst { it.second == id }
            _isSelectItem.value.removeAt(index)
            return true
        }
        return false
    }

    fun deleteSelectList(userId: Long, accessToken: String, hasInternet: Boolean): Boolean {
        if (!hasInternet) {
            _usersStateFlow.value = ApiStateUser.Error(R.string.no_internet_connection)
            return false
        }

        val contactList = _selectContacts.value.toMutableList()

        for (contact in contactList) {
            deleteContactFromList(userId, accessToken, contact, true)
            deleteSelectContact(contact)
        }

        _selectContacts.value = contactList
        return true
    }

    fun changeListInMultiselectMode(contact: Contact) {
        if (!selectContacts.value.contains(contact)) {
            addSelectContact(contact)
        } else {
            deleteSelectContact(contact)
        }
        if (selectContacts.value.isEmpty()) {
            changeMultiselectMode()
        }
    }

    fun changeMultiselectMode() {
        _isMultiselect.value = !_isMultiselect.value
        if (!isMultiselect.value) {
            _selectContacts.value = emptyList()
            _isSelectItem.value.clear()
        }
    }

    fun deleteStates() {
        UserDataHolder.states.clear()
    }

    fun changeStates() {
        _usersStateFlow.value = ApiStateUser.Initial
    }

    fun showNotification(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1, notificationBuilder.build())
        }
    }

    fun getUser(): ResponseOfUser.Data = UserDataHolder.userData

}