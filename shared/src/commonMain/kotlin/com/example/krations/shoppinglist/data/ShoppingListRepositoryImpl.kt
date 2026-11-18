package com.example.krations.shoppinglist.data

import com.example.krations.shoppinglist.data.local.dao.ShoppingListItemDao
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItemEntity
import com.example.krations.shoppinglist.domain.ShoppingListRepository
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock.System.now
import kotlin.uuid.Uuid

class ShoppingListRepositoryImpl(private val dao: ShoppingListItemDao) : ShoppingListRepository {
    override fun observeItems(): Flow<List<ShoppingListItem>> = dao.getShoppingListItems().map {
        it.map(ShoppingListItemEntity::toShoppingListItem)
    }

    override suspend fun getItem(id: String): ShoppingListItem? =
        dao.getItemById(id)?.toShoppingListItem()

    override suspend fun addItem(
        name: String, quantityNeeded: Double, unit: String, productId: String?
    ) = now().toString().let { timestamp ->
        dao.insertItem(
            ShoppingListItemEntity(
                id = Uuid.random().toString(),
                productId = productId,
                name = name,
                quantityNeeded = quantityNeeded,
                unit = unit,
                isPurchased = false,
                purchasedQuantity = 0.0,
                purchasedAt = null,
                deletedAt = null,
                createdAt = timestamp,
                updatedAt = timestamp,
                ownerUserId = "local-user", // TODO: get real user from account data
            )
        )
    }

    override suspend fun updateItem(item: ShoppingListItem) {
        val existing = dao.getItemById(item.id) ?: return
        dao.updateItem(
            existing.copy(
                productId = item.productId,
                name = item.name,
                quantityNeeded = item.quantityNeeded,
                unit = item.unit,
                isPurchased = item.isPurchased,
                purchasedQuantity = item.purchasedQuantity,
                purchasedAt = item.purchasedAt,
                updatedAt = now().toString(),
            )
        )
    }

    override suspend fun deleteItem(id: String) = now().toString().let { timestamp ->
        dao.deleteItem(
            deletedAt = timestamp,
            updatedAt = timestamp,
            id = id,
        )
    }

    override suspend fun deletePurchasedItems() =
        dao.deletePurchasedItems(now().toString())
}