package com.example.krations.alerts.domain.model

import kotlinx.datetime.LocalDate

data class ExpiringItem(
    val batchId: String,
    val productName: String,
    val expirationDate: LocalDate,
    val expiryAlertDays: Int,
)