package com.example.krations

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.krations.alerts.ui.ExpirationRemindersEffect

@Composable
fun App() {
    MaterialTheme {
        AuthRoute()
        ExpirationRemindersEffect()
    }
}
