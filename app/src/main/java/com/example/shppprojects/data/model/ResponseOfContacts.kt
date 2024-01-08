package com.example.shppprojects.data.model

data class ResponseOfContacts (
    val status : String,
    val code : Int,
    val message : String? = null,
    val data : Data
)
{ data class Data (val contacts : ArrayList<UserData>?) }