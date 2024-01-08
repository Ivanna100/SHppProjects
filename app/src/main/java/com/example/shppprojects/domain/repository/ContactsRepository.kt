package com.example.shppprojects.domain.repository

import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.UserData
import com.example.shppprojects.domain.state.ArrayDataApiResultState

interface ContactsRepository {

    suspend fun addContact(
        userId: Long,
        accessToken: String,
        contact: Contact
    ): ArrayDataApiResultState

    suspend fun getUserContacts(userId: Long, accessToken: String): ArrayDataApiResultState

    suspend fun getAllUsers(accessToken: String, user: UserData): ArrayDataApiResultState
    suspend fun deleteContact(
        userId: Long,
        accessToken: String,
        contactId: Long
    ): ArrayDataApiResultState
}