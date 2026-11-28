package com.example.krations.shoppinglist.data

import com.example.krations.core.data.CurrentUserProvider
import com.example.krations.shoppinglist.data.local.dao.ShoppingListItemDao
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItemEntity
import com.example.krations.shoppinglist.domain.ShoppingListRepository
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlin.time.Clock.System.now
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class)
class ShoppingListRepositoryImpl(
    private val dao: ShoppingListItemDao,
    private val currentUserProvider: CurrentUserProvider,
) : ShoppingListRepository {
    override fun observeItems(): Flow<List<ShoppingListItem>> =
        currentUserProvider.userId.flatMapLatest { userId ->
            dao.getShoppingListItems(userId).map { it.map(ShoppingListItemEntity::toShoppingListItem) }
        }

    override suspend fun getItem(id: String): ShoppingListItem? =
        dao.getItemById(id, currentUserProvider.userId.value)?.toShoppingListItem()

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
                ownerUserId = currentUserProvider.userId.value,
            )
        )
    }

    override suspend fun updateItem(item: ShoppingListItem) {
        val userId = currentUserProvider.userId.value
        val existing = dao.getItemById(item.id, userId) ?: return
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
            ownerUserId = currentUserProvider.userId.value,
        )
    }

    override suspend fun deletePurchasedItems() =
        dao.deletePurchasedItems(now().toString(), currentUserProvider.userId.value)

    override suspend fun setItemPurchased(id: String) =
        dao.setPurchased(id, now().toString(), currentUserProvider.userId.value)

    override suspend fun setAllItemsPurchased() =
        dao.setPurchasedAll(now().toString(), currentUserProvider.userId.value)
}
