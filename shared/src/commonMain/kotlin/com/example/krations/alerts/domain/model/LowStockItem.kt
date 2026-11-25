package com.example.krations.alerts.domain.model

data class LowStockItem(
    val productName: String,
    val unit: String,
    val stock: Double,
    val minQuantity: Double,
)
