package com.example.krations.alerts.domain

import com.example.krations.alerts.domain.model.LowStockItem
import kotlinx.coroutines.flow.Flow

interface LowStockItemsRepository {
    fun observeItems(): Flow<List<LowStockItem>>
}
