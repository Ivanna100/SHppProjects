package com.example.shppprojects.data.model

data class ResponseOfContacts(
    val status: String,
    val code: String,
    val message: String?,
    val data: Data,
) {
    data class Data(val contacts: List<UserData>?)
}