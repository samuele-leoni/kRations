package com.example.krations.shoppinglist.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class ShoppingListItem(
    @PrimaryKey
    val id: String,
    // TODO add foreign key ref
    val ownerUserId: String,
    // TODO add foreign key ref
    val productId: String?,
    val name: String,
    val quantityNeeded: Double,
    val unit: String,
    val isPurchased: Boolean,
    val purchasedQuantity: Double,
    val purchasedAt: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)
