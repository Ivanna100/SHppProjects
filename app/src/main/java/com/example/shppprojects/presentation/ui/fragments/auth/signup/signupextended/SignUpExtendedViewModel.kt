package com.example.shppprojects.presentation.ui.fragments.auth.signup.signupextended

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.data.model.UserRequest
import com.example.shppprojects.data.repository.AccountRepositoryImpl
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.utils.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpExtendedViewModel @Inject constructor(private val accountRepImpl: AccountRepositoryImpl) :
    ViewModel() {

    private val _registerStateFlow = MutableStateFlow<ApiStateUser>(ApiStateUser.Initial)
    val registerState: StateFlow<ApiStateUser> = _registerStateFlow

    fun isLogout() {
        _registerStateFlow.value = ApiStateUser.Initial
    }

    fun registerUser(body: UserRequest) = viewModelScope.launch(Dispatchers.IO) {
        _registerStateFlow.value = ApiStateUser.Loading
        _registerStateFlow.value = accountRepImpl.registerUser(body)
    }

    fun saveUserDataToDataStore(context: Context, email: String, password: String) =
        viewModelScope.launch(Dispatchers.IO) {
            DataStore.saveData(context, email, password)
        }

}