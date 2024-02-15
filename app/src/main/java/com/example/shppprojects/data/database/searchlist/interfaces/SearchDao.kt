package com.example.shppprojects.data.database.searchlist.interfaces

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shppprojects.data.database.searchlist.SearchDbEntity

@Dao
interface SearchDao {

    @Insert(entity = SearchDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(users: List<SearchDbEntity>)

    @Query("SELECT * FROM search_list")
    suspend fun getList(): List<SearchDbEntity>

    @Insert(entity = SearchDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: SearchDbEntity)

    @Delete(entity = SearchDbEntity::class)
    suspend fun deleteUser(user: SearchDbEntity)

    @Query("DELETE FROM search_list")
    suspend fun deleteAllUsers()

}