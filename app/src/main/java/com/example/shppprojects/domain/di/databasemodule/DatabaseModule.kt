package com.example.shppprojects.domain.di.databasemodule

import android.content.Context
import androidx.room.Room
import com.example.shppprojects.data.database.AppDatabase
import com.example.shppprojects.data.database.contactslist.interfaces.ContactsDao
import com.example.shppprojects.data.database.searchlist.interfaces.SearchDao
import com.example.shppprojects.data.database.userslist.interfaces.UsersDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideChannelSearchDao(database: AppDatabase): SearchDao {
        return database.getSearchDao()
    }

    @Provides
    fun provideChannelContactsDao(database: AppDatabase): ContactsDao {
        return database.getContactsDao()
    }

    @Provides
    fun provideChannelUsersDao(database: AppDatabase): UsersDao {
        return database.getUsersDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "database.db"
        ).build()
    }

}