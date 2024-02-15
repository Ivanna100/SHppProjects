package com.example.shppprojects.data.database.contactslist.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shppprojects.data.database.contactslist.ContactsDbEntity

@Dao
interface ContactsDao {

    @Insert(entity = ContactsDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addContacts(contacts: List<ContactsDbEntity>)

    @Query("SELECT * FROM contacts_list")
    suspend fun getContacts(): List<ContactsDbEntity>

    @Query("DELETE FROM contacts_list")
    suspend fun deleteAllContacts()

}