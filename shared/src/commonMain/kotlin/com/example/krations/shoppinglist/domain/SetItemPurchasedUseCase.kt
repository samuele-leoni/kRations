package com.example.krations.shoppinglist.domain

import co.touchlab.kermit.Logger
import com.example.krations.inventory.domain.InventoryBatchRepository
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import kotlin.time.Clock.System.now

class SetItemPurchasedUseCase(
    private val shoppingListRepository: ShoppingListRepository,
    private val inventoryBatchRepository: InventoryBatchRepository,
) {
    suspend operator fun invoke(item: ShoppingListItem, quantity: Double, expirationDate: String?) {
        Logger.withTag("SetItemPurchased").d { "${item.name}: productId=${item.productId}, qty=$quantity, exp=$expirationDate" }
        item.productId?.let { inventoryBatchRepository.addQuantity(it, quantity, expirationDate) }
        shoppingListRepository.updateItem(
            item.copy(isPurchased = true, purchasedQuantity = quantity, purchasedAt = now().toString())
        )
    }
}