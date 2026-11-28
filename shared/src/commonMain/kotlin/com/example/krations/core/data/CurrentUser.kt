package com.example.krations.core.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal const val LOCAL_USER_ID = "local-user"

class CurrentUserProvider {
    private val mutableUserId = MutableStateFlow(LOCAL_USER_ID)
    val userId: StateFlow<String> = mutableUserId.asStateFlow()

    fun setAuthenticatedUser(userId: String?) {
        mutableUserId.value = userId?.takeIf(String::isNotBlank) ?: LOCAL_USER_ID
    }
}
