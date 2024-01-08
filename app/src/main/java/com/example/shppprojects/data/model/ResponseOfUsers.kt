package com.example.shppprojects.data.model

data class ResponceOfUsers (
    val status : String,
    val code : Int,
    val message : String? = null,
    val data : Data
) { data class Data ( val users : ArrayList<UserData>?)}