package com.example.shppprojects.presentation.ui.fragments.contactprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.R
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.data.userdataholder.UserDataHolder
import com.example.shppprojects.domain.repository.ContactsRepository
import com.example.shppprojects.domain.state.ApiStateUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactProfileViewModel @Inject constructor(
    private val contactsRepositoryImpl: ContactsRepository,
) : ViewModel() {

    private val _usersStateFlow = MutableStateFlow<ApiStateUser>(ApiStateUser.Initial)
    val usersState: StateFlow<ApiStateUser> = _usersStateFlow

    private var alreadyAdded = false

    fun addContact(userId: Long, contact: Contact, accessToken: String, hasInterner: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            if (!hasInterner) {
                _usersStateFlow.value = ApiStateUser.Error(R.string.no_internet_connection)
                return@launch
            }
            if (alreadyAdded) return@launch
            alreadyAdded = true

            _usersStateFlow.value = ApiStateUser.Loading
            _usersStateFlow.value = contactsRepositoryImpl.addContact(
                userId,
                accessToken,
                contact
            )
        }

    fun changeState() {
        _usersStateFlow.value = ApiStateUser.Initial
    }

    fun requestGetUser(): ResponseOfUser.Data = UserDataHolder.userData

}