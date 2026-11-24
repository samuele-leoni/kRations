package com.example.krations.alerts.domain

import com.example.krations.alerts.domain.model.ExpiringItem
import kotlinx.datetime.LocalDateTime

class RescheduleExpirationRemindersUseCase(
    private val notifier: ExpirationNotifier,
) {
    operator fun invoke(items: List<ExpiringItem>, now: LocalDateTime) {
        items
            .mapNotNull { item ->
                ExpirationRules.reminderDate(item.expirationDate, now)
                    ?.let { expirationReminderDate -> item to expirationReminderDate }
            }
            .sortedBy { (_, expirationReminderDate) -> expirationReminderDate }
            .take(MAX_SCHEDULED)
            .forEach { (item, expirationReminderDate) ->
                notifier.cancel(item.batchId)
                notifier.schedule(item, expirationReminderDate)
            }
    }

    private companion object {
        const val MAX_SCHEDULED = 64
    }
}