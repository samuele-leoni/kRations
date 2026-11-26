package com.example.krations.home.ui

import androidx.compose.runtime.Composable
import com.example.krations.core.navigation.AppNavigation
import com.example.krations.core.navigation.rememberAppNavigationState

@Composable
fun HomeScreen(onProfileClick: () -> Unit) {
    val navigationState = rememberAppNavigationState()

    AppNavigation(
        state = navigationState,
        onProfileClick = onProfileClick,
    )
}
