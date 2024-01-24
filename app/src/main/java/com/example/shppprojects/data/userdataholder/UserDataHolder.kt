package com.example.shppprojects.data.userdataholder

import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.data.model.ResponseOfUser
import com.example.shppprojects.domain.state.ApiStateUser

object UserDataHolder {

    var states: ArrayList<Pair<Long, ApiStateUser>> = ArrayList()

    var serverUsers: List<Contact> = listOf()

    var serverContacts: List<Contact> = listOf()

    lateinit var userData: ResponseOfUser.Data

}