package com.example.shppprojects.data.repository

import android.content.Context
import com.example.shppprojects.R
import com.example.shppprojects.data.userdataholder.UserDataHolder
import com.example.shppprojects.domain.network.AccountApiService
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.data.model.UserRequest
import com.example.shppprojects.domain.repository.AccountRepository
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.utils.Constants
import com.example.shppprojects.presentation.utils.Constants.AUTHORIZATION_PREFIX
import com.example.shppprojects.presentation.utils.DataStore
import retrofit2.http.Body
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val service: AccountApiService,
) : AccountRepository {

    override suspend fun registerUser(@Body body: UserRequest): ApiStateUser {
        return try {
            val response = service.registerUser(body)
            response.data?.let { UserDataHolder.userData = it }
            response.data?.let { ApiStateUser.Success(it) }
                ?: ApiStateUser.Error(R.string.invalid_request)
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.register_error_user_exist)
        }
    }

    override suspend fun authorizeUser(email: String, password: String): ApiStateUser {
        return try {
            val response = service.authorizeUser(email, password)
            response.data?.let { UserDataHolder.userData = it }
            response.data?.let { ApiStateUser.Success(it) }
                ?: ApiStateUser.Error(R.string.invalid_request)
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.not_correct_input)
        }
    }

    override suspend fun getUser(userId: Long, accessToken: String): ApiStateUser {
        return try {
            val response = service.getUser(userId, "$AUTHORIZATION_PREFIX $accessToken")
            response.data?.let { ApiStateUser.Success(it) }
                ?: ApiStateUser.Error(R.string.invalid_request)
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.invalid_request)
        }
    }

    override suspend fun editUser(
        userId: Long,
        accessToken: String,
        name: String,
        career: String?,
        phone: String,
        address: String?,
        birthday: Date?,
        refreshToken: String,
    ): ApiStateUser {
        return try {
            val response = service.editUser(
                userId,
                "$AUTHORIZATION_PREFIX $accessToken",
                name,
                career,
                phone,
                address,
                birthday
            )
            response.data?.let {
                UserDataHolder.userData = ResponseOfUser.Data(it.user, accessToken, refreshToken)
            }
            response.data?.let { ApiStateUser.Success(it) }
                ?: ApiStateUser.Error(R.string.invalid_request)
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.invalid_request)
        }
    }

    override suspend fun autoLogin(context: Context): ApiStateUser {
        return try {
            val response = service.authorizeUser(
                DataStore.getDataFromKey(context, Constants.KEY_EMAIL).toString(),
                DataStore.getDataFromKey(context, Constants.KEY_PASSWORD).toString()
            )
            response.data?.let { UserDataHolder.userData = it }
            response.data?.let { ApiStateUser.Success(it) } ?: ApiStateUser.Error(
                R.string.invalid_request
            )
        } catch (e: Exception) {
            ApiStateUser.Error(R.string.automatic_login_error)
        }
    }

}