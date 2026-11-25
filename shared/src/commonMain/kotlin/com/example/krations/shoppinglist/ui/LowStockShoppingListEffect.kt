package com.example.krations.shoppinglist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.krations.shoppinglist.domain.AddNeedsRestockItemsUseCase
import org.koin.compose.koinInject

@Composable
fun LowStockShoppingListEffect() {
    val addNeedsRestockItems = koinInject<AddNeedsRestockItemsUseCase>()

    LaunchedEffect(Unit) {
        addNeedsRestockItems()
    }
}
