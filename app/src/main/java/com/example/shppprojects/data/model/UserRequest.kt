package com.example.shppprojects.data.model

import java.io.File

data class UserRequest (
    val email : String? = null,
    val password : String? = null,
    val username : String? = null,
    val phone : String? = null,
    val image : File? = null
)