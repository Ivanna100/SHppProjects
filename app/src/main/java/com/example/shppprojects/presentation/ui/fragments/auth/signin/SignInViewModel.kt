package com.example.shppprojects.presentation.ui.fragments.auth.signin

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.domain.repository.AccountRepository
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.utils.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val accountRepositoryImpl: AccountRepository,
) : ViewModel() {

    private val _authorizationStateFlow = MutableStateFlow<ApiStateUser>(ApiStateUser.Initial)
    val authorizationState: StateFlow<ApiStateUser> = _authorizationStateFlow

    fun authorizationUser(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        _authorizationStateFlow.value = ApiStateUser.Loading
        _authorizationStateFlow.value = accountRepositoryImpl.authorizeUser(email, password)
    }

    fun saveDataToDataStore(context: Context, email: String, password: String) {
        viewModelScope.launch {
            DataStore.saveData(context, email, password)
        }
    }

}