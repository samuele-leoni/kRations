package com.example.krations.core.navigation

import androidx.navigation3.runtime.NavKey

sealed interface AppRoute : NavKey {
    data object Home : AppRoute

    data object Inventory : AppRoute

    data object ShoppingList : AppRoute

    data object AddProduct : AppRoute

    data class EditProduct(val productId: String) : AppRoute

    data object Categories : AppRoute

    data object NewCategory : AppRoute

    data class EditCategory(val categoryId: String) : AppRoute
}
