package com.example.krations.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.krations.inventory.ui.InventoryScreen
import com.example.krations.inventory.ui.category.CategoryEditScreen
import com.example.krations.inventory.ui.category.CategoryListScreen
import com.example.krations.inventory.ui.product.ProductEditScreen
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
                    InventoryScreen(
                        onAddProduct = { backStack.add(AppRoute.AddProduct) },
                        onEditProduct = { productId -> backStack.add(AppRoute.EditProduct(productId)) },
                        onManageCategories = { backStack.add(AppRoute.Categories) },
                    )
                }

                is AppRoute.AddProduct -> NavEntry(key) {
                    ProductEditScreen(
                        productId = null,
                        onCreateCategory = { backStack.add(AppRoute.NewCategory) },
                        onDone = { backStack.removeLastOrNull() },
                    )
                }

                is AppRoute.EditProduct -> NavEntry(key) {
                    ProductEditScreen(
                        productId = key.productId,
                        onCreateCategory = { backStack.add(AppRoute.NewCategory) },
                        onDone = { backStack.removeLastOrNull() },
                    )
                }

                is AppRoute.Categories -> NavEntry(key) {
                    CategoryListScreen(
                        onBack = { backStack.removeLastOrNull() },
                        onNewCategory = { backStack.add(AppRoute.NewCategory) },
                        onEditCategory = { categoryId -> backStack.add(AppRoute.EditCategory(categoryId)) },
                    )
                }

                is AppRoute.NewCategory -> NavEntry(key) {
                    CategoryEditScreen(
                        categoryId = null,
                        onDone = { backStack.removeLastOrNull() },
                    )
                }

                is AppRoute.EditCategory -> NavEntry(key) {
                    CategoryEditScreen(
                        categoryId = key.categoryId,
                        onDone = { backStack.removeLastOrNull() },
                    )
                }

                is AppRoute.ShoppingList -> NavEntry(key) {
                    ShoppingListScreen()
                }
            }
        },
    )
}
