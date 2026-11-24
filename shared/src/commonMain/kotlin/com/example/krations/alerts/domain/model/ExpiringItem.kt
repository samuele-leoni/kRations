package com.example.krations.alerts.domain.model

import kotlinx.datetime.LocalDate

data class ExpiringItem(
    // TODO understand whether to use the batchId or something else
    val batchId: String,
    val productName: String,
    val expirationDate: LocalDate,
)