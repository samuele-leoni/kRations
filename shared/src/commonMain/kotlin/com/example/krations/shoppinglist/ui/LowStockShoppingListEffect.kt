package com.example.krations.shoppinglist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.krations.shoppinglist.domain.AddLowStockItemsUseCase
import org.koin.compose.koinInject

@Composable
fun LowStockShoppingListEffect() {
    val addLowStockItems = koinInject<AddLowStockItemsUseCase>()

    LaunchedEffect(Unit) {
        addLowStockItems()
    }
}
