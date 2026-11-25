package com.example.krations.alerts.domain

import com.example.krations.alerts.domain.model.LowStockItem
import kotlinx.datetime.LocalDateTime

class RescheduleLowStockRemindersUseCase(
    private val notifier: LowStockNotifier,
) {

    operator fun invoke(items: List<LowStockItem>, now: LocalDateTime) {
        items
            .filterNot { it.needsRestock }
            .forEach { item -> notifier.cancel(item.productId) }

        items
            .filter { it.needsRestock }
            .sortedBy { it.productName }
            .take(MAX_SCHEDULED)
            .forEach { item ->
                notifier.cancel(item.productId)
                notifier.schedule(item, LowStockRules.reminderDate(now))
            }
    }

    private companion object {
        const val MAX_SCHEDULED = 64
    }
}
