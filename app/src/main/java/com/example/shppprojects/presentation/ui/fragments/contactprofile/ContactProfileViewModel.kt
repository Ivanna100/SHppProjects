package com.example.shppprojects.presentation.ui.fragments.contactprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.domain.repository.ContactsRepository
import com.example.shppprojects.domain.state.ArrayDataApiResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactProfileViewModel @Inject constructor(
    private val contactsRepositoryImpl: ContactsRepository
) : ViewModel(){
    private val _usersStateFlow = MutableStateFlow<ArrayDataApiResultState>(
        ArrayDataApiResultState.Initial
    )
    val usersState : StateFlow<ArrayDataApiResultState> = _usersStateFlow

    private var alreadyAdded = false

    fun addContact(userId: Long, contact: Contact, accessToken: String) =
        viewModelScope.launch(Dispatchers.IO) {
            if(alreadyAdded) return@launch
            alreadyAdded = true
            _usersStateFlow.value = ArrayDataApiResultState.Loading
            _usersStateFlow.value = contactsRepositoryImpl.addContact(
                userId,
                accessToken,
                contact)
        }
}