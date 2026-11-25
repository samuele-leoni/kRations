package com.example.krations.alerts.domain

import com.example.krations.alerts.domain.model.LowStockItem
import kotlinx.datetime.LocalDateTime

class RescheduleLowStockRemindersUseCase(
    private val notifier: LowStockNotifier,
) {

    operator fun invoke(items: List<LowStockItem>, now: LocalDateTime) {
        notifier.cancel()
        if (items.isEmpty()) return

        notifier.schedule(items, LowStockRules.reminderDate(now))
    }
}
