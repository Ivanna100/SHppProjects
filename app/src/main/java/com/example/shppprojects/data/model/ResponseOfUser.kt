package com.example.shppprojects.data.model

data class ResponseOfUser(
    val status: String? = null,
    val code: Int? = null,
    val message: String? = null,
    val data: Data? = null,
) {
    data class Data(val user: UserData, val accessToken: String, val refreshToken: String)
}