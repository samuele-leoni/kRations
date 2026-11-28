package com.example.krations.home.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import com.example.krations.core.navigation.AppNavigation
import com.example.krations.core.navigation.rememberAppNavigationState

@Composable
fun HomeScreen(
    onProfileClick: () -> Unit,
    onSyncClick: () -> Unit,
    isSyncing: Boolean,
) {
    val navigationState = rememberAppNavigationState()
    val syncingState = rememberUpdatedState(isSyncing)

    AppNavigation(
        state = navigationState,
        onProfileClick = onProfileClick,
        onSyncClick = onSyncClick,
        isSyncing = syncingState,
    )
}
