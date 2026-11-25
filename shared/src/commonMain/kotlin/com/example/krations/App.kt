package com.example.krations

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.krations.alerts.ui.ExpirationRemindersEffect
import com.example.krations.shoppinglist.ui.LowStockShoppingListEffect

@Composable
fun App() {
    MaterialTheme {
        AuthRoute()
        ExpirationRemindersEffect()
        LowStockShoppingListEffect()
    }
}
