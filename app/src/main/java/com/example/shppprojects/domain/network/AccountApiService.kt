package com.example.shppprojects.domain.network

import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.data.model.UserRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface AccountApiService {

    @POST("users")
    suspend fun registerUser(@Body body: UserRequest): ResponseOfUser

    @POST("login")
    suspend fun authorizeUser(
        @Query("email") email: String,
        @Query("password") password: String,
    ): ResponseOfUser

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: Long,
        @Header("Authorization") accessToken: String,
    ): ResponseOfUser

    @FormUrlEncoded
    @PUT("users/{userId}")
    suspend fun editUser(
        @Path("userId") userId: Long,
        @Header("Authorization") accessToken: String,
        @Field("name") name: String, @Field("career") career: String?,
        @Field("phone") phone: String, @Field("address") address: String?,
        @Field("birthday") birthday: Date?,
    ): ResponseOfUser

}