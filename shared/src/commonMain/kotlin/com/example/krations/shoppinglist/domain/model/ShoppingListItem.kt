package com.example.krations.shoppinglist.domain.model

data class ShoppingListItem(
    val id: String,
    val productId: String?,
    val name: String,
    val quantityNeeded: Double,
    val unit: String,
    val isPurchased: Boolean,
    val purchasedQuantity: Double,
    val purchasedAt: String?,
)
