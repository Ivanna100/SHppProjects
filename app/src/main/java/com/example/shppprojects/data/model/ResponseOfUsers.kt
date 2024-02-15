package com.example.shppprojects.data.model

class ResponseOfUsers(
    val status: String,
    val code: String,
    val message: String?,
    val data: Data,
) {
    data class Data(val users: List<UserData>?)
}