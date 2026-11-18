package com.example.krations.inventory.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    indices = [Index("categoryId")],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL,
        ),
    ],
)
data class ProductEntity(
    @PrimaryKey
    val id: String,
    // TODO add foreign key ref
    val ownerUserId: String,
    val categoryId: String?,
    val name: String,
    val description: String?,
    val iconName: String?,
    val unit: String = "unit",
    val quantity: Double = 0.0,
    val minQuantity: Double = 0.0,
    val expiryAlertDays: Int = 7,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)