package com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.data.repository.AccountRepositoryImpl
import com.example.shppprojects.domain.state.UserApiResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(private val accountReposImpl : AccountRepositoryImpl)
    : ViewModel() {

    private val _editProfileStateFlow = MutableStateFlow<UserApiResultState> (UserApiResultState.Initial)
    val editUserState : StateFlow<UserApiResultState> = _editProfileStateFlow

    fun editUser(
        userId: Long,
        accessToken: String,
        name: String,
        career: String? = null,
        phone: String,
        address: String? = null,
        date: Date? = null) = viewModelScope.launch {
        _editProfileStateFlow.value = UserApiResultState.Loading
        _editProfileStateFlow.value = accountReposImpl.editUser(userId, accessToken,
            name, career, phone, address, date)
    }
}