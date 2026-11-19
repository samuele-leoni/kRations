package com.example.krations.inventory.domain

import com.example.krations.inventory.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeCategories(): Flow<List<Category>>

    suspend fun addCategory(name: String, iconName: String = "package")

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(id: String)
}
