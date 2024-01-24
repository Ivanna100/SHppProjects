package com.example.shppprojects.data.database.userslist

import com.example.shppprojects.data.database.userslist.interfaces.UsersDao
import com.example.shppprojects.data.database.userslist.interfaces.UsersListRepository
import javax.inject.Inject

class RoomUsersListRepository @Inject constructor(
    private val usersDao: UsersDao,
) : UsersListRepository {
    override suspend fun addUsers(users: List<UsersDbEntity>) {
        deleteAllUsers()
        usersDao.addUsers(users)
    }

    override suspend fun getUsers(): List<UsersDbEntity> = usersDao.getUsers()

    override suspend fun updateUsers(users: List<UsersDbEntity>) {
        usersDao.updateUsers(users)
    }

    override suspend fun deleteAllUsers() {
        usersDao.deleteAllUsers()
    }

}