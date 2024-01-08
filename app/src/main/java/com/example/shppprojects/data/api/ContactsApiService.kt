package com.example.shppprojects.data.api

import com.example.shppprojects.data.model.ResponceOfUsers
import com.example.shppprojects.data.model.ResponseOfContacts
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContactsApiService {

    @GET("users")
    suspend fun getAllUsers (@Header("Authorization") accessToken : String) : ResponceOfUsers

    @FormUrlEncoded
    @PUT("users/{userId}/contacts")
    suspend fun addContact (
        @Path("userId") userId : Long,
        @Header("Authorization") accessToken : String,
        @Field("contactId") contactId : Long
    ) : ResponseOfContacts


    @DELETE("users/{userId}/contacts/{contactId}")
    suspend fun deleteContact (
        @Path("userId") userId : Long,
        @Path("contactId") contactId : Long,
        @Header("Authorization") accessToken : String) : ResponseOfContacts

    @GET("users/{userId}/contacts")
    suspend fun getUserContacts (
        @Path("userId") userId : Long,
        @Header("Authorization") accessToken : String) : ResponseOfContacts

}