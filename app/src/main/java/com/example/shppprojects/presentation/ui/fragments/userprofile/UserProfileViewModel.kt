package com.example.shppprojects.presentation.ui.fragments.userprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.data.repository.AccountRepositoryImpl
import com.example.shppprojects.domain.state.UserApiResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(private val accountReposImpl : AccountRepositoryImpl)
    : ViewModel() {

    private val _getUserStateFlow = MutableStateFlow<UserApiResultState> (UserApiResultState.Initial)
    val getUserState : StateFlow<UserApiResultState> = _getUserStateFlow

    fun getUser(userId : Long, accessToken : String) = viewModelScope.launch(Dispatchers.IO) {
        _getUserStateFlow.value = UserApiResultState.Loading
        _getUserStateFlow.value = accountReposImpl.getUser(userId, accessToken)
    }

}