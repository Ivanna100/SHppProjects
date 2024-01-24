package com.example.shppprojects.presentation.utils

object Constants {

    // server
    const val BASE_URL = "http://178.63.9.114:7777/api/"

    const val DIALOG_TAG = "add_contact_dialog"
    const val LOG_TAG = "LOG_TAG"
    const val TRANSITION_NAME_IMAGE = "TRANSITION_NAME_IMAGE"
    const val TRANSITION_NAME_NAME = "TRANSITION_NAME_FULL_NAME"
    const val TRANSITION_NAME_CAREER = "TRANSITION_NAME_CAREER"

    // data store
    const val REGISTER_DATA_STORE = "data store"
    const val KEY_EMAIL = "email"
    const val KEY_PASSWORD = "password"
    const val KEY_REMEMBER_ME = "remember me"

    // screens in viewpager
    const val FRAGMENT_COUNT = 2
    const val PROFILE_FRAGMENT = 0
    const val CONTACTS_FRAGMENT = 1

    // date
    const val OUTPUT_DATE_FORMAT = "dd/MM/yyyy"
    const val INPUT_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"

    // validation
    const val PASSWORD_REGEX =
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{7,}\$"

    // authorization prefix
    const val AUTHORIZATION_PREFIX = "Bearer"

    // notification button
    const val CHANNEL_ID = "channel id"
    const val CHANNEL_NAME = "channel name"

    /* sign in accounts
    val userALogin = "user.a@gmail.com"
    val userAPassword = "userA12345"

    val userBLogin = "user.b@gmail.com"
    val userBPassword = "userB12345" */
}