package com.example.krations.inventory.data.local.entity

import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    indices = [Index(value = ["id", "ownerUserId"], unique = true), Index(value = ["ownerUserId", "name"])],
)
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val ownerUserId: String,
    val name: String,
    val iconName: String = "package",
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)
