package com.example.krations.alerts.domain

import com.example.krations.alerts.domain.model.ExpiringItem
import com.example.krations.shoppinglist.domain.ShoppingListRepository
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import kotlinx.coroutines.flow.first
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class AddExpiringItemsToShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
) {
    suspend operator fun invoke(items: List<ExpiringItem>) {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        val listItems = shoppingListRepository.observeItems().first()

        items
            .filter { ExpirationRules.needsReplacement(it.expirationDate, today, it.expiryAlertDays) }
            .distinctBy { it.productId }
            .filterNot { expiring -> listItems.any { it.isAbout(expiring) } }
            .forEach {
                shoppingListRepository.addItem(
                    name = it.productName,
                    quantityNeeded = it.quantity,
                    unit = it.unit,
                    productId = it.productId,
                )
            }
    }

    private fun ShoppingListItem.isAbout(item: ExpiringItem): Boolean =
        productId == item.productId || name.trim().equals(item.productName.trim(), ignoreCase = true)
}