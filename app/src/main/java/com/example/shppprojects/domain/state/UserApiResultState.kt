package com.example.shppprojects.domain.state

import com.example.shppprojects.data.model.ResponseOfUser

sealed class UserApiResultState {

    object Initial : UserApiResultState()
    data class Success( val data : ResponseOfUser.Data) : UserApiResultState()

    data class Error(val error: Int) : UserApiResultState()

    object Loading : UserApiResultState()
}