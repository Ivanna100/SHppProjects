package com.example.shppprojects.data.repository

import com.example.shppprojects.R
import com.example.shppprojects.data.api.AccountApiService
import com.example.shppprojects.data.model.UserRequest
import com.example.shppprojects.domain.repository.AccountRepository
import com.example.shppprojects.domain.state.UserApiResultState
import com.example.shppprojects.utils.Constants.AUTHORIZATION_PREFIX
import com.example.shppprojects.utils.ext.log
import retrofit2.http.Body
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val service: AccountApiService,
) : AccountRepository {

    override suspend fun registerUser(@Body body : UserRequest) : UserApiResultState {
        return try {
            val response = service.registerUser(body)
            log("response register user: $response")
            response.data?.let { UserApiResultState.Success(it) } ?:
            UserApiResultState.Error(R.string.invalid_request)
        } catch (e : Exception) {
            log("exception register user: $e")
            UserApiResultState.Error(R.string.register_error_user_exist)
        }
    }

    override suspend fun authorizeUser(@Body body : UserRequest) : UserApiResultState {
        return try {
            val response = service.authorizeUser(body)
            response.data?.let { UserApiResultState.Success(it) } ?:
            UserApiResultState.Error(R.string.invalid_request)
        } catch (e : Exception) {
            UserApiResultState.Error(R.string.not_correct_input)
        }
    }

    override suspend fun getUser(userId : Long, accessToken : String) : UserApiResultState {
        return try {
            val response = service.getUser(userId, "$AUTHORIZATION_PREFIX $accessToken")
            response.data?.let { UserApiResultState.Success(it) } ?:
            UserApiResultState.Error(R.string.invalid_request)
        } catch (e : Exception) {
            UserApiResultState.Error(R.string.invalid_request)
        }
    }

    override suspend fun editUser(userId: Long, accessToken: String, name : String, career: String?,
                                  phone: String, address: String?, birthday: Date?) : UserApiResultState {
        return try {
            val response = service.editUser(userId, "$AUTHORIZATION_PREFIX $accessToken",
                name, career, phone, address, birthday)
            log("response edit user: $response")
            response.data?.let { UserApiResultState.Success(it) } ?:
            UserApiResultState.Error(R.string.invalid_request)
        } catch (e : Exception) {
            log("edit user exception: ${e.toString()}")
            UserApiResultState.Error(R.string.invalid_request)
        }
    }

    override suspend fun autoLogin(email : String, password : String) : UserApiResultState {
        return try {
            val response = service.authorizeUser(
                UserRequest(
                    email = email,
                    password = password
                )
            )
            log("autologin: $response")
            response.data?.let { UserApiResultState.Success(it) } ?: UserApiResultState.Error(
                R.string.invalid_request
            )
        } catch (e : Exception) {
            log("autologin exception: $e")
            UserApiResultState.Error(R.string.automatic_login_error)
        }
    }

}