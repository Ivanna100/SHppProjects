package com.example.shppprojects.data.database.userslist.interfaces

import com.example.shppprojects.data.database.userslist.UsersDbEntity

interface UsersListRepository : UsersDao {

    override suspend fun addUsers(users: List<UsersDbEntity>)

    override suspend fun getUsers(): List<UsersDbEntity>

    override suspend fun updateUsers(users: List<UsersDbEntity>)

    override suspend fun deleteAllUsers()

}