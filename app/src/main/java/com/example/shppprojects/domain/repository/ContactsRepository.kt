package com.example.shppprojects.domain.repository

import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.UserData
import com.example.shppprojects.domain.state.ApiStateUser

interface ContactsRepository {

    suspend fun addContact(
        userId: Long,
        accessToken: String,
        contact: Contact,
    ): ApiStateUser

    suspend fun getUserContacts(userId: Long, accessToken: String): ApiStateUser

    suspend fun getAllUsers(accessToken: String, user: UserData): ApiStateUser
    suspend fun deleteContact(
        userId: Long,
        accessToken: String,
        contact: Contact,
    ): ApiStateUser
}