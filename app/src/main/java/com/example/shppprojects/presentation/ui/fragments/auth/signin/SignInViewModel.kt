package com.example.shppprojects.presentation.ui.fragments.auth.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.data.model.UserRequest
import com.example.shppprojects.domain.repository.AccountRepository
import com.example.shppprojects.domain.state.UserApiResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountRepositoryImpl : AccountRepository
) : ViewModel() {

    private val _authorizationStateFlow = MutableStateFlow<UserApiResultState>(UserApiResultState.Initial)
    val authorizationState : StateFlow<UserApiResultState> = _authorizationStateFlow

    fun authorizationUser(body : UserRequest) = viewModelScope.launch(Dispatchers.IO) {
        _authorizationStateFlow.value = UserApiResultState.Loading
        _authorizationStateFlow.value = accountRepositoryImpl.authorizeUser(body)
    }
}