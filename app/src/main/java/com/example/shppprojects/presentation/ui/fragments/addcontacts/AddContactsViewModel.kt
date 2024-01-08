package com.example.shppprojects.presentation.ui.fragments.addcontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.R
import com.example.shppprojects.data.UserDataHolder
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.UserData
import com.example.shppprojects.domain.repository.ContactsRepository
import com.example.shppprojects.domain.state.ArrayDataApiResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    private val contactsRepository : ContactsRepository
) : ViewModel() {

    private val _usersStateFlow = MutableStateFlow<ArrayDataApiResultState>(ArrayDataApiResultState.Initial)
    val usersState : StateFlow<ArrayDataApiResultState> = _usersStateFlow

    private val _users = MutableStateFlow<List<Contact>>(listOf())
    val users : StateFlow<List<Contact>> = _users

    private val _states : MutableStateFlow<ArrayList<Pair<Long, ArrayDataApiResultState>>> =
        MutableStateFlow(ArrayList())
    val states : StateFlow<ArrayList<Pair<Long, ArrayDataApiResultState>>> = _states

    // so that it does not always depend on the server
    val supportList: ArrayList<Contact> = arrayListOf()
    // search helper
    private var startedListContact: List<Contact> = listOf()

    fun getAllUsers(accessToken : String, user : UserData) = viewModelScope.launch(Dispatchers.IO) {
        _usersStateFlow.value = ArrayDataApiResultState.Loading
        _usersStateFlow.value = contactsRepository.getAllUsers(accessToken, user)
        withContext(Dispatchers.Main) {
            _users.value = UserDataHolder.getServerList()
            startedListContact = _users.value
        }
    }

    fun addContact(userId : Long, contact: Contact, accessToken: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if(!supportList.contains(contact)) {
                supportList.add(contact)
                _states.value = arrayListOf(Pair(contact.id, ArrayDataApiResultState.Loading))
                contactsRepository.addContact(userId, accessToken, contact)
                _states.value = UserDataHolder.getStates()
            } else {
                _usersStateFlow.value = ArrayDataApiResultState.Error(R.string.already_have_this_a_contact)
            }
        }

    fun updateContactList(newText : String?) : Int {
        val filteredList = startedListContact.filter { contact: Contact ->
            contact.name?.contains(newText ?: "", ignoreCase = true) == true
        }
        _users.value =filteredList
        return filteredList.size
    }

}