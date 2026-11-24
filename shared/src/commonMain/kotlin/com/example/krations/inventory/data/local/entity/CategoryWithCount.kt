package com.example.krations.inventory.data.local.entity

import androidx.room3.Embedded

data class CategoryWithCount(
    @Embedded val category: CategoryEntity,
    val productCount: Long,
)
