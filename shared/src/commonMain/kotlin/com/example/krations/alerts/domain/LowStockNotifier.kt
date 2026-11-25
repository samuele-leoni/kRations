package com.example.krations.alerts.domain

import com.example.krations.alerts.domain.model.LowStockItem
import kotlinx.datetime.LocalDateTime

interface LowStockNotifier {

    fun schedule(items: List<LowStockItem>, reminderDate: LocalDateTime)

    fun cancel()
}
