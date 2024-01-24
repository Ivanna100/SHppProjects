package com.example.shppprojects.data.database.userslist.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shppprojects.data.database.userslist.UsersDbEntity

@Dao
interface UsersDao {

    @Insert(entity = UsersDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUsers(users: List<UsersDbEntity>)

    @Query("SELECT * FROM users_list")
    suspend fun getUsers(): List<UsersDbEntity>

    @Update(entity = UsersDbEntity::class)
    suspend fun updateUsers(users: List<UsersDbEntity>)

    @Query("DELETE FROM users_list")
    suspend fun deleteAllUsers()

}