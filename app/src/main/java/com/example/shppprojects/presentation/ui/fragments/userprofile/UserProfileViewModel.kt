package com.example.shppprojects.presentation.ui.fragments.userprofile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.data.userdataholder.UserDataHolder
import com.example.shppprojects.presentation.utils.Constants
import com.example.shppprojects.presentation.utils.DataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor() :
    ViewModel() {

    fun getUser(): ResponseOfUser.Data = UserDataHolder.userData

    fun removeDataFromDataStore(context: Context) = viewModelScope.launch(Dispatchers.IO) {
        DataStore.deleteDataFromDataStore(context, Constants.KEY_EMAIL)
        DataStore.deleteDataFromDataStore(context, Constants.KEY_PASSWORD)
        DataStore.deleteDataFromDataStore(context, Constants.KEY_REMEMBER_ME)
    }

}