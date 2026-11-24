package com.example.krations.inventory.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    indices = [
        Index(value = ["categoryId", "ownerUserId"]),
        Index(value = ["id", "ownerUserId"], unique = true),
        Index(value = ["ownerUserId", "name"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id", "ownerUserId"],
            childColumns = ["categoryId", "ownerUserId"],
            onDelete = ForeignKey.RESTRICT,
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
    val minQuantity: Double = 0.0,
    val expiryAlertDays: Int = 7,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)
