package com.example.shppprojects.presentation.ui.fragments.userprofile.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.R
import com.example.shppprojects.data.userdataholder.UserDataHolder
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.data.repository.AccountRepositoryImpl
import com.example.shppprojects.domain.state.ApiStateUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val accountReposImpl: AccountRepositoryImpl,
) : ViewModel() {

    private val _editProfileStateFlow = MutableStateFlow<ApiStateUser>(ApiStateUser.Initial)
    val editUserState: StateFlow<ApiStateUser> = _editProfileStateFlow

    fun editUser(
        userId: Long,
        accessToken: String,
        name: String,
        career: String? = null,
        phone: String,
        address: String? = null,
        date: Date? = null,
        refreshToken: String,
        hasInternet: Boolean,
    ) = viewModelScope.launch {
        _editProfileStateFlow.value = ApiStateUser.Loading
        if (hasInternet) {
            _editProfileStateFlow.value = accountReposImpl.editUser(
                userId, accessToken,
                name, career, phone, address, date, refreshToken
            )
        } else {
            _editProfileStateFlow.value = ApiStateUser.Error(R.string.no_internet_connection)
        }
    }

    fun getUser(): ResponseOfUser.Data = UserDataHolder.userData

}