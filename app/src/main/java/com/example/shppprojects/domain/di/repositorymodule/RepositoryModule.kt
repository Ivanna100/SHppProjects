package com.example.shppprojects.domain.di.repositorymodule

import com.example.shppprojects.data.repository.AccountRepositoryImpl
import com.example.shppprojects.data.repository.ContactsRepositoryImpl
import com.example.shppprojects.domain.repository.AccountRepository
import com.example.shppprojects.domain.repository.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        accountRepository: AccountRepositoryImpl,
    ): AccountRepository


    @Binds
    @Singleton
    abstract fun bindContactsRepository(
        contactsRepository: ContactsRepositoryImpl,
    ): ContactsRepository

}