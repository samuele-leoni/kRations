package com.example.krations

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.krations.core.navigation.AppNavigation

@Composable
fun App() {
    MaterialTheme {
        AppNavigation()
    }
}
