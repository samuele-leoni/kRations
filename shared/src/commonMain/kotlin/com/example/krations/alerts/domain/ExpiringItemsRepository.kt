package com.example.krations.alerts.domain

import com.example.krations.alerts.domain.model.ExpiringItem
import kotlinx.coroutines.flow.Flow

interface ExpiringItemsRepository {
    fun observeItems(): Flow<List<ExpiringItem>>
}