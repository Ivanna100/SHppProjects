package com.example.shppprojects.utils

object ErrorMessage {

    fun getErrorMessage(error : Int) : String {
        val meaning = when(error) {
            400 -> "Invalid request"
            401 -> "Unauthorized"
            403 -> "Access denied"
            404 -> "Contact not found"
            else -> "Unknown error "
        }
        return meaning
    }

}
