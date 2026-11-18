package com.example.krations.inventory.data.local.entity

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    // TODO add foreign key ref
    val ownerUserId: String,
    val name: String,
    val iconName: String?,
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
)
