package com.example.shppprojects.utils

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
    const val FIRST_FRAGMENT = 0
    const val SECOND_FRAGMENT = 1

    // date
    const val OUTPUT_DATE_FORMAT = "dd/MM/yyyy"
    const val INPUT_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"

    // validation
    const val PASSWORD_REGEX =
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d]{7,}\$"

    // authorization prefix
    const val AUTHORIZATION_PREFIX = "Bearer"

    // country mobile code
    const val MOBILE_CODE = "US"

    // error message
    const val OPERATION_ERROR = "Operation error!"

    // notification button
    const val NOTIFICATION_ACTION = "search contacts"
    const val CHANNEL_ID = "channel id"
    const val CHANNEL_NAME = "channel name"
}