package com.example.krations.inventory.data.local.entity

import androidx.room3.Embedded

data class ProductWithStock(
    @Embedded val product: ProductEntity,
    val totalQuantity: Double,
)
