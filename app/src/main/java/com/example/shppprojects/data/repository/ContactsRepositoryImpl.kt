package com.example.shppprojects.data.repository

import com.example.shppprojects.R
import com.example.shppprojects.data.UserDataHolder
import com.example.shppprojects.data.api.ContactsApiService
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.UserData
import com.example.shppprojects.domain.repository.ContactsRepository
import com.example.shppprojects.domain.state.ArrayDataApiResultState
import com.example.shppprojects.utils.Constants.AUTHORIZATION_PREFIX
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(
    private val service: ContactsApiService
) : ContactsRepository {

    override suspend fun getAllUsers(accessToken: String, user : UserData): ArrayDataApiResultState {
        return try {
            val response = service.getAllUsers("$AUTHORIZATION_PREFIX$accessToken")
            val contacts = UserDataHolder.getContacts()
            val filteredUsers = response.data.users?.filter {
                it.name != null && it.email != user.email && !contacts.contains(it.toContact())
            }
            val users : MutableStateFlow<List<Contact>> = MutableStateFlow(filteredUsers?.map {
                it.toContact()} ?: emptyList())
            UserDataHolder.setServerList(users)
            response.data.let { ArrayDataApiResultState.Success(it.users) }
        } catch (e: Exception) {
            ArrayDataApiResultState.Error(R.string.invalid_request)
        }
    }

    override suspend fun addContact(
        userId: Long,
        accessToken: String,
        contact: Contact
    ): ArrayDataApiResultState {
        val states : ArrayList<Pair<Long, ArrayDataApiResultState>> = ArrayList()
        states.add(Pair(contact.id, ArrayDataApiResultState.Loading))
        return try {
            val response =
                service.addContact(userId, "${AUTHORIZATION_PREFIX}$accessToken", contact.id)
            states[states.size-1] =
                Pair(contact.id, ArrayDataApiResultState.Success(response.data.contacts))
            UserDataHolder.setStates(states[states.size - 1])
            response.data.let { ArrayDataApiResultState.Success(it.contacts) }
        } catch (e: Exception) {
            ArrayDataApiResultState.Error(R.string.invalid_request)
        }
    }

    override suspend fun deleteContact(
        userId: Long,
        accessToken: String,
        contactId : Long
    ): ArrayDataApiResultState {
        return try {
            val response = service.deleteContact(
                userId, contactId,"${AUTHORIZATION_PREFIX}$accessToken"

            )
            response.data.let { ArrayDataApiResultState.Success(it.contacts) }
        } catch (e: Exception) {
            ArrayDataApiResultState.Error(R.string.invalid_request)
        }
    }

    override suspend fun getUserContacts(userId: Long, accessToken: String): ArrayDataApiResultState {
        return try {
            val response = service.getUserContacts(userId, "${AUTHORIZATION_PREFIX}$accessToken")
            UserDataHolder.setContactList((response.data.contacts?.map { it.toContact() }
                ?: emptyList()) as ArrayList<Contact>)
            response.data.let { ArrayDataApiResultState.Success(it.contacts) }
        } catch (e: Exception) {
            ArrayDataApiResultState.Error(R.string.invalid_request)
        }
    }

}