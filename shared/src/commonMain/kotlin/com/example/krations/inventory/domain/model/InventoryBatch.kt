package com.example.krations.inventory.domain.model

data class InventoryBatch(
    val id: String,
    val productId: String,
    val quantity: Double,
    val expirationDate: String?,
)
