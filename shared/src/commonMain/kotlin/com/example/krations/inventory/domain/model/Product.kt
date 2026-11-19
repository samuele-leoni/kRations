package com.example.krations.inventory.domain.model

data class Product(
    val id: String,
    val name: String,
    val description: String?,
    val unit: String,
    val iconName: String?,
    val categoryId: String?,
    val minQuantity: Double,
    val stock: Double,
)
