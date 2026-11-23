package com.example.krations.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.krations.inventory.ui.InventoryScreen
import com.example.krations.shoppinglist.ui.ShoppingListScreen

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<AppRoute>(AppRoute.Inventory) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is AppRoute.Inventory -> NavEntry(key) {
                    InventoryScreen()
                }

                is AppRoute.ShoppingList -> NavEntry(key) {
                    ShoppingListScreen()
                }
            }
        },
    )
}
