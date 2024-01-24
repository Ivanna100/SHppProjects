package com.example.shppprojects.presentation.ui.fragments.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.domain.repository.AccountRepository
import com.example.shppprojects.domain.state.ApiStateUser
import com.example.shppprojects.presentation.utils.Constants
import com.example.shppprojects.presentation.utils.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val accountRepositoryImpl: AccountRepository) :
    ViewModel() {

    private val _authorizationStateFlow = MutableStateFlow<ApiStateUser>(ApiStateUser.Initial)
    val authorizationState: StateFlow<ApiStateUser> = _authorizationStateFlow

    fun autoLogin(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        _authorizationStateFlow.value = ApiStateUser.Loading
        _authorizationStateFlow.value = accountRepositoryImpl.autoLogin(context)
    }

    suspend fun isAutoLogin(context: Context): Boolean = withContext(Dispatchers.IO) {
        DataStore.getDataFromKey(context, Constants.KEY_REMEMBER_ME) != null
    }

}