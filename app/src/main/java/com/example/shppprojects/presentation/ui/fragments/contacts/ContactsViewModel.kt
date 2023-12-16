package com.example.shppprojects.presentation.ui.fragments.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.R
import com.example.shppprojects.data.UserDataHolder
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.repository.ContactsRepositoryImpl
import com.example.shppprojects.domain.state.ArrayDataApiResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(private val contactsRepositoryImpl: ContactsRepositoryImpl)
    : ViewModel() {

    private val _usersStateFlow = MutableStateFlow<ArrayDataApiResultState>(ArrayDataApiResultState.Initial)
    val usersState: StateFlow<ArrayDataApiResultState> = _usersStateFlow

    private val _contactList = MutableStateFlow(listOf<Contact>())
    val contactList: StateFlow<List<Contact>> get() = _contactList

    private val _selectContacts = MutableStateFlow<List<Contact>>(listOf())
    val selectContacts : StateFlow<List<Contact>> get() = _selectContacts

    val isMultiselect = MutableLiveData(false)

    private val _isSelectItem : MutableStateFlow<ArrayList<Pair<Boolean, Int>>> =
        MutableStateFlow(ArrayList())
    val isSelectItem : StateFlow<ArrayList<Pair<Boolean, Int>>> = _isSelectItem

    private val startedListContact : ArrayList<Contact> = arrayListOf()

    fun initialContactList(userId : Long, accessToken : String) =
        viewModelScope.launch(Dispatchers.IO){
            _usersStateFlow.value = ArrayDataApiResultState.Loading
            _usersStateFlow.value = contactsRepositoryImpl.getUserContacts(userId, accessToken)
            _contactList.value = UserDataHolder.getContacts()
            startedListContact.clear()
            startedListContact.addAll(_contactList.value)
        }

    fun addContact(userId : Long, contact: Contact, accessToken: String) =
        viewModelScope.launch(Dispatchers.IO){
            _usersStateFlow.value = ArrayDataApiResultState.Loading
            _usersStateFlow.value = contactsRepositoryImpl.addContact(userId, accessToken, contact,)
            val contactList = _contactList.value?.toMutableList() ?: mutableListOf()
        }

    fun addContactToList(userId : Long, contact: Contact, accessToken: String, position : Int =
        _contactList.value.size) : Boolean {
        val contactList = _contactList.value.toMutableList()
        if(!contactList.contains(contact)) {
            contactList.add(position, contact)
            _contactList.value = contactList
            addContact(userId, contact, accessToken)
            return true
        }
        return false
    }

    fun addSelectContact(contact: Contact) : Boolean {
        val contactList = _selectContacts.value.toMutableList()
        if(!contactList.contains(contact)) {
            contactList.add(contact)
            _selectContacts.value = contactList
            _isSelectItem.value.add(Pair(true, contact.id.toInt()))
            return true
        }
        return false
    }

    fun deleteContact(userId : Long, accessToken: String, contactId : Long) =
        viewModelScope.launch(Dispatchers.IO) {
            _usersStateFlow.value = contactsRepositoryImpl.deleteContact(userId, accessToken, contactId)
        }


    fun deleteSelectContact(contact: Contact): Boolean {
        val contactList = _selectContacts.value.toMutableList()
        if(contactList.contains(contact)){
            contactList.remove(contact)
            _selectContacts.value = contactList
            val id = contact.id.toInt()
            val index = _isSelectItem.value.indexOfFirst { it.second == id }
            _isSelectItem.value.removeAt(index)
            return true
        }
        return false
    }

    fun deleteContactFromList(userId: Long, accessToken: String, contactId: Long) : Boolean {
        val contact = _contactList.value.find { it.id == contactId }
        val contactList = _contactList.value.toMutableList()

        if(contactList.contains(contact)) {
            deleteContact(userId, accessToken, contactId)
            contactList.remove(contact)
            _contactList.value = contactList
            startedListContact.remove(contact)
            return true
        }
        _usersStateFlow.value = ArrayDataApiResultState.Error(R.string.contact_not_found)
        return false
    }

    fun deleteSelectList(userId : Long, accessToken: String) {
        val contactList = _selectContacts.value.toMutableList()

        for(contact in contactList) {
            deleteContactFromList(userId, accessToken, contact.id)
            deleteSelectContact(contact)
        }

        _selectContacts.value = contactList
    }

    fun changeMultiselectMode() {
        isMultiselect.value = !isMultiselect.value!!
        if(isMultiselect.value == false) {
            _selectContacts.value = emptyList()
            _isSelectItem.value.clear()
        }
    }

    fun updateContactList(newtext : String?) : Int {
        val filteredList = startedListContact.filter { contact: Contact ->
            contact.name?.contains( newtext ?: "", ignoreCase = true) == true
        }
        _contactList.value = filteredList
        return filteredList.size
    }

    fun deleteStates() {
        UserDataHolder.deleteStates()
    }

}