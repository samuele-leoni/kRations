package com.example.krations.inventory.data.local.entity

import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    indices = [
        Index(value = ["productId", "ownerUserId", "expirationDate"]),
        Index(value = ["ownerUserId", "expirationDate"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id", "ownerUserId"],
            childColumns = ["productId", "ownerUserId"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE,
        ),
    ],
)
data class InventoryBatchEntity(
    @PrimaryKey
    val id: String,
    val ownerUserId: String,
    val productId: String,
    val quantity: Double,
    val expirationDate: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)
