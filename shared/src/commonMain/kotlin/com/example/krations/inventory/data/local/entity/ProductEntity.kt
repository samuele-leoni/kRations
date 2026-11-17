package com.example.krations.inventory.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey
    val id: String,
    // TODO add foreign key ref
    val ownerUserId: String,
    // TODO add foreign key ref
    val categoryId: String?,
    val name: String,
    val description: String?,
    val iconName: String?,
    val unit: String = "unit",
    val minQuantity: Double = 0.0,
    val expiryAlertDays: Int = 7,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)