package com.example.krations.shoppinglist.domain

import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow

interface ShoppingListRepository {
    fun observeItems(): Flow<List<ShoppingListItem>>
    suspend fun getItem(id: String): ShoppingListItem?
    suspend fun addItem(name: String, quantityNeeded: Double, unit: String, productId: String? = null)
    suspend fun updateItem(item: ShoppingListItem)
    suspend fun deleteItem(id: String)
    suspend fun deletePurchasedItems()
}