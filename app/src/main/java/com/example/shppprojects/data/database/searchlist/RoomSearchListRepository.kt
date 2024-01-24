package com.example.shppprojects.data.database.searchlist

import com.example.shppprojects.data.database.searchlist.interfaces.SearchDao
import com.example.shppprojects.data.database.searchlist.interfaces.SearchListRepository
import javax.inject.Inject

class RoomSearchListRepository @Inject constructor(private val searchDao: SearchDao) :
    SearchListRepository {
    override suspend fun addList(users: List<SearchDbEntity>) {
        deleteAllUsers()
        searchDao.addList(users)
    }

    override suspend fun getList(): List<SearchDbEntity> = searchDao.getList()

    override suspend fun addUser(user: SearchDbEntity) {
        searchDao.addUser(user)
    }

    override suspend fun deleteUser(user: SearchDbEntity) {
        searchDao.deleteUser(user)
    }

    override suspend fun deleteAllUsers() {
        searchDao.deleteAllUsers()
    }

}