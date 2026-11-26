package com.example.krations.core.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Home
import com.composables.icons.materialsymbols.rounded.Inventory
import com.composables.icons.materialsymbols.rounded.Shopping_cart

enum class NavSection(
    val label: String,
    val icon: ImageVector,
    val rootRoute: AppRoute,
    val headerTitle: String = label,
) {
    Home("Home", MaterialSymbols.Rounded.Home, AppRoute.Home, "kRations"),
    Inventory("Inventory", MaterialSymbols.Rounded.Inventory, AppRoute.Inventory),
    ShoppingList("Shopping list", MaterialSymbols.Rounded.Shopping_cart, AppRoute.ShoppingList),
}
