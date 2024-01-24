package com.example.shppprojects.data.database.searchlist.interfaces

import com.example.shppprojects.data.database.searchlist.SearchDbEntity

interface SearchListRepository : SearchDao {

    override suspend fun addList(users: List<SearchDbEntity>)

    override suspend fun getList(): List<SearchDbEntity>

    override suspend fun addUser(user: SearchDbEntity)

    override suspend fun deleteUser(user: SearchDbEntity)

    override suspend fun deleteAllUsers()
}