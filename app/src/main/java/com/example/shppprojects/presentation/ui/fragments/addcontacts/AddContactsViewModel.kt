package com.example.shppprojects.presentation.ui.fragments.addcontacts

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
import com.example.shppprojects.data.model.UserData
import com.example.shppprojects.domain.repository.ContactsRepository
import com.example.shppprojects.domain.state.ApiStateUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val databaseImpl: DatabaseImpl,
    private val notificationBuilder: NotificationCompat.Builder,
    private val notificationManager: NotificationManagerCompat,
) : ViewModel() {

    private val _usersStateFlow = MutableStateFlow<ApiStateUser>(ApiStateUser.Initial)
    val usersState: StateFlow<ApiStateUser> = _usersStateFlow

    private val _users = MutableStateFlow<List<Contact>>(listOf())
    val users: StateFlow<List<Contact>> = _users

    private val _states: MutableStateFlow<ArrayList<Pair<Long, ApiStateUser>>> =
        MutableStateFlow(ArrayList())
    val states: StateFlow<ArrayList<Pair<Long, ApiStateUser>>> = _states

    // so that it does not always depend on the server
    val supportList: ArrayList<Contact> = arrayListOf()

    fun getAllUsers(accessToken: String, user: UserData, hasInternet: Boolean) =
        viewModelScope.launch(Dispatchers.Main) {
            _usersStateFlow.value = ApiStateUser.Loading
            _usersStateFlow.value = if (hasInternet) {
                contactsRepository.getAllUsers(accessToken, user)
            } else {
                databaseImpl.getAllUsers()
            }
            _users.value = UserDataHolder.serverUsers
            databaseImpl.addUsersToSearchList(_users.value)
        }


    fun addContact(userId: Long, contact: Contact, accessToken: String, hasInternet: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            if (!supportList.contains(contact)) {
                supportList.add(contact)
                _states.value = arrayListOf(Pair(contact.id, ApiStateUser.Loading))
                if (hasInternet) {
                    contactsRepository.addContact(userId, accessToken, contact)
                } else {
                    _usersStateFlow.value = ApiStateUser.Error(R.string.no_internet_connection)
                }

                _states.value = UserDataHolder.states
                databaseImpl.deleteFromSearchList(contact)
            }
        }

    fun changeState() {
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

    fun requestGetUser(): ResponseOfUser.Data = UserDataHolder.userData

}