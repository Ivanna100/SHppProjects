package com.example.shppprojects.data.database.contactslist.interfaces

import com.example.shppprojects.data.database.contactslist.ContactsDbEntity

interface ContactsListRepository : ContactsDao {

    override suspend fun addContacts(contacts: List<ContactsDbEntity>)

    override suspend fun getContacts(): List<ContactsDbEntity>

    override suspend fun deleteAllContacts()

}