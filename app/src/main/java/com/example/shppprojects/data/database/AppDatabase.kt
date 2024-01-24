package com.example.shppprojects.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shppprojects.data.database.contactslist.interfaces.ContactsDao
import com.example.shppprojects.data.database.contactslist.ContactsDbEntity
import com.example.shppprojects.data.database.searchlist.interfaces.SearchDao
import com.example.shppprojects.data.database.searchlist.SearchDbEntity
import com.example.shppprojects.data.database.userslist.interfaces.UsersDao
import com.example.shppprojects.data.database.userslist.UsersDbEntity

@Database(
    version = 1,
    entities = [
        SearchDbEntity::class,
        ContactsDbEntity::class,
        UsersDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSearchDao(): SearchDao

    abstract fun getContactsDao(): ContactsDao

    abstract fun getUsersDao(): UsersDao

}