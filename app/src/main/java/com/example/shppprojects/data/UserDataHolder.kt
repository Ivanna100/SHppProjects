package com.example.shppprojects.data

import com.example.shppprojects.data.model.Contact
import com.example.shppprojects.domain.state.ArrayDataApiResultState
import kotlinx.coroutines.flow.MutableStateFlow

object UserDataHolder {

    private val serverUsers = MutableStateFlow<List<Contact>>(listOf())
    private var contacts = ArrayList<Contact>()
    private var states: ArrayList<Pair<Long, ArrayDataApiResultState>> = ArrayList()

    fun setServerList(serverUsers: MutableStateFlow<List<Contact>>) {
        this.serverUsers.value = serverUsers.value
    }

    fun setContactList(contacts: ArrayList<Contact>) {
        this.contacts = contacts
    }

    fun setStates(state : Pair<Long, ArrayDataApiResultState>) {
        states.add(state)
    }

    fun getServerList(): List<Contact> = serverUsers.value

    fun getContacts(): ArrayList<Contact> = contacts

    fun getStates() : ArrayList<Pair<Long, ArrayDataApiResultState>> = states

    fun deleteStates() { states.clear() }

}