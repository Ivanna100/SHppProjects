package com.example.shppprojects.data.database

import com.example.shppprojects.R
import com.example.shppprojects.data.userdataholder.UserDataHolder
import com.example.shppprojects.data.database.contactslist.RoomContactsListRepository
import com.example.shppprojects.data.database.searchlist.RoomSearchListRepository
import com.example.shppprojects.data.database.searchlist.SearchDbEntity
import com.example.shppprojects.data.database.userslist.RoomUsersListRepository
import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.UserData
import com.example.shppprojects.domain.state.ApiStateUser
import javax.inject.Inject

class DatabaseImpl @Inject constructor(
    private val roomSearchListRepository: RoomSearchListRepository,
    private val roomContactsListRepository: RoomContactsListRepository,
    private val roomUsersListRepository: RoomUsersListRepository,
) {
    suspend fun getAllUsers(): ApiStateUser {
        return try {
            val response = roomUsersListRepository.getUsers()
            UserDataHolder.serverUsers = response.map { entity -> entity.toContact() }
            ApiStateUser.Success<ArrayList<UserData>>(arrayListOf())
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.error_occurred)
        }
    }

    suspend fun getContacts(): ApiStateUser {
        return try {
            val response = roomContactsListRepository.getContacts()
            UserDataHolder.serverContacts = response.map { entity -> entity.toContact() }
            ApiStateUser.Success<ArrayList<UserData>>(arrayListOf())
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.error_occurred)
        }
    }

    suspend fun getSearchList(): List<Contact> =
        roomSearchListRepository.getList().map { entity -> entity.toContact() }

    suspend fun addToSearchList(contact: Contact) {
        roomSearchListRepository.addUser(SearchDbEntity.toEntity(contact))
    }

    suspend fun deleteFromSearchList(contact: Contact) {
        roomSearchListRepository.deleteUser(SearchDbEntity.toEntity(contact))
    }

    suspend fun addUsersToSearchList(users: List<Contact>) {
        roomSearchListRepository.addList(users.map { contact -> SearchDbEntity.toEntity(contact) })
    }

}