package com.example.shppprojects.data.repository

import com.example.shppprojects.R
import com.example.shppprojects.data.userdataholder.UserDataHolder
import com.example.shppprojects.domain.network.ContactsApiService
import com.example.shppprojects.data.database.contactslist.ContactsDbEntity
import com.example.shppprojects.data.database.contactslist.RoomContactsListRepository
import com.example.shppprojects.data.database.userslist.RoomUsersListRepository
import com.example.shppprojects.data.database.userslist.UsersDbEntity
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.UserData
import com.example.shppprojects.domain.repository.ContactsRepository
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.utils.Constants.AUTHORIZATION_PREFIX
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactsRepositoryImpl @Inject constructor(
    private val service: ContactsApiService,
    private val roomUsersListRepository: RoomUsersListRepository,
    private val roomContactsListRepository: RoomContactsListRepository,
) : ContactsRepository {

    override suspend fun getAllUsers(accessToken: String, user: UserData): ApiStateUser {
        return try {
            val response = service.getAllUsers("$AUTHORIZATION_PREFIX $accessToken")
            val contacts = UserDataHolder.serverContacts
            val filteredUsers = response.data.users?.filter {
                it.name != null && it.email != user.email && !contacts.contains(it.toContact())
            }
            val users = filteredUsers?.map { it.toContact() } ?: emptyList()
            UserDataHolder.serverUsers = users
            roomUsersListRepository.addUsers(users.map { contact -> UsersDbEntity.toEntity(contact) })
            response.data.let { ApiStateUser.Success(it.users) }
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.invalid_request)
        }
    }

    override suspend fun addContact(
        userId: Long,
        accessToken: String,
        contact: Contact,
    ): ApiStateUser {
        val states: ArrayList<Pair<Long, ApiStateUser>> = ArrayList()
        states.add(Pair(contact.id, ApiStateUser.Loading))
        return try {
            val response =
                service.addContact(
                    userId,
                    "$AUTHORIZATION_PREFIX $accessToken",
                    contact.id
                )
            UserDataHolder.states.add(contact.id to ApiStateUser.Success(response.data.users))
            response.data.let { ApiStateUser.Success(it.users) }
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.invalid_request)
        }
    }

    override suspend fun deleteContact(
        userId: Long,
        accessToken: String,
        contact: Contact,
    ): ApiStateUser {
        return try {
            val response = service.deleteContact(
                userId, contact.id, "$AUTHORIZATION_PREFIX $accessToken"

            )
            response.data.let { ApiStateUser.Success(it.users) }
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.invalid_request)
        }
    }

    override suspend fun getUserContacts(userId: Long, accessToken: String): ApiStateUser {
        return try {
            val response = service.getUserContacts(userId, "$AUTHORIZATION_PREFIX $accessToken")
            val users = response.data.contacts?.map { it.toContact() } ?: emptyList()
            UserDataHolder.serverContacts = users
            roomContactsListRepository.addContacts(users.map { ContactsDbEntity.toEntity(it) })
            response.data.let { ApiStateUser.Success(it.contacts) }
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.invalid_request)
        }
    }

}