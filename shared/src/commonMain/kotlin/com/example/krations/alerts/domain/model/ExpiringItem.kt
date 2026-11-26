package com.example.krations.alerts.domain.model

import kotlinx.datetime.LocalDate

data class ExpiringItem(
    val batchId: String,
    val productId: String,
    val productName: String,
    val unit: String,
    val quantity: Double,
    val expirationDate: LocalDate,
    val expiryAlertDays: Int,
)