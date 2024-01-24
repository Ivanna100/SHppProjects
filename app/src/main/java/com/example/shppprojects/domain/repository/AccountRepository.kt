package com.example.shppprojects.domain.repository

import android.content.Context
import com.example.shppprojects.data.model.UserRequest
import com.example.shppprojects.domain.state.ApiStateUser
import java.util.Date

interface AccountRepository {

    suspend fun registerUser(body: UserRequest): ApiStateUser

    suspend fun authorizeUser(email: String, password: String): ApiStateUser

    suspend fun getUser(userId: Long, accessToken: String): ApiStateUser

    suspend fun editUser(
        userId: Long,
        accessToken: String,
        name: String,
        career: String?,
        phone: String,
        address: String?,
        birthday: Date?,
        refreshToken: String,
    ): ApiStateUser

    suspend fun autoLogin(context: Context): ApiStateUser

}