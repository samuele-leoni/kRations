package com.example.krations.alerts.domain

import com.example.krations.alerts.domain.model.ExpiringItem
import kotlinx.datetime.LocalDateTime

interface ExpirationNotifier {
    fun schedule(item: ExpiringItem, expirationReminderDate: LocalDateTime)
    fun cancel(batchId: String)
}