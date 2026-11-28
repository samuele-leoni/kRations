package com.example.krations.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.krations.core.navigation.ui.SectionScaffold
import com.example.krations.inventory.ui.InventoryScreen
import com.example.krations.inventory.ui.category.CategoryEditScreen
import com.example.krations.inventory.ui.category.CategoryListScreen
import com.example.krations.inventory.ui.product.ProductEditScreen
import com.example.krations.shoppinglist.ui.ShoppingListScreen

@Composable
fun AppNavigation(
    state: AppNavigationState,
    onProfileClick: () -> Unit,
    onSyncClick: () -> Unit,
    isSyncing: State<Boolean>,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        modifier = modifier,
        backStack = state.backStack,
        onBack = { state.pop() },
        entryProvider = { key ->
            when (key) {
                AppRoute.Home -> NavEntry(key) {
                    SectionScaffold(
                        section = NavSection.Home,
                        onSelectSection = state::select,
                        onProfileClick = onProfileClick,
                        onSyncClick = onSyncClick,
                        isSyncing = isSyncing,
                        content = {},
                    )
                }

                AppRoute.Inventory -> NavEntry(key) {
                    SectionScaffold(
                        section = NavSection.Inventory,
                        onSelectSection = state::select,
                        onProfileClick = onProfileClick,
                        onSyncClick = onSyncClick,
                        isSyncing = isSyncing,
                    ) {
                        InventoryScreen(
                            onAddProduct = { state.push(AppRoute.AddProduct) },
                            onEditProduct = { productId -> state.push(AppRoute.EditProduct(productId)) },
                            onManageCategories = { state.push(AppRoute.Categories) },
                        )
                    }
                }

                AppRoute.ShoppingList -> NavEntry(key) {
                    SectionScaffold(
                        section = NavSection.ShoppingList,
                        onSelectSection = state::select,
                        onProfileClick = onProfileClick,
                        onSyncClick = onSyncClick,
                        isSyncing = isSyncing,
                    ) {
                        ShoppingListScreen(modifier = Modifier.fillMaxSize())
                    }
                }

                AppRoute.AddProduct -> NavEntry(key) {
                    ProductEditScreen(
                        productId = null,
                        onCreateCategory = { state.push(AppRoute.NewCategory) },
                        onDone = { state.pop() },
                    )
                }

                is AppRoute.EditProduct -> NavEntry(key) {
                    ProductEditScreen(
                        productId = key.productId,
                        onCreateCategory = { state.push(AppRoute.NewCategory) },
                        onDone = { state.pop() },
                    )
                }

                AppRoute.Categories -> NavEntry(key) {
                    CategoryListScreen(
                        onBack = { state.pop() },
                        onNewCategory = { state.push(AppRoute.NewCategory) },
                        onEditCategory = { categoryId -> state.push(AppRoute.EditCategory(categoryId)) },
                    )
                }

                AppRoute.NewCategory -> NavEntry(key) {
                    CategoryEditScreen(
                        categoryId = null,
                        onDone = { state.pop() },
                    )
                }

                is AppRoute.EditCategory -> NavEntry(key) {
                    CategoryEditScreen(
                        categoryId = key.categoryId,
                        onDone = { state.pop() },
                    )
                }
            }
        },
    )
}
