package com.example.krations.core.navigation

import androidx.navigation3.runtime.NavKey

sealed interface AppRoute : NavKey {
    data object Inventory : AppRoute

    data object ShoppingList : AppRoute
}
