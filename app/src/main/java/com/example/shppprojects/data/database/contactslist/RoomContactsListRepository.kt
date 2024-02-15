package com.example.shppprojects.data.database.contactslist

import com.example.shppprojects.data.database.contactslist.interfaces.ContactsDao
import com.example.shppprojects.data.database.contactslist.interfaces.ContactsListRepository
import javax.inject.Inject

class RoomContactsListRepository @Inject constructor(
    private val contactsDao: ContactsDao,
) : ContactsListRepository {
    override suspend fun addContacts(contacts: List<ContactsDbEntity>) {
        deleteAllContacts()
        contactsDao.addContacts(contacts)
    }

    override suspend fun getContacts(): List<ContactsDbEntity> = contactsDao.getContacts()

    override suspend fun deleteAllContacts() {
        contactsDao.deleteAllContacts()
    }

}