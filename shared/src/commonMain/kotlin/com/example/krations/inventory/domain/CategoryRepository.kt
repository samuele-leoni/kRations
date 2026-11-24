package com.example.krations.inventory.domain

import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.domain.model.CategorySummary
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeCategories(): Flow<List<Category>>

    fun observeCategoriesWithCount(): Flow<List<CategorySummary>>

    suspend fun getCategory(id: String): Category?

    suspend fun addCategory(name: String, iconName: String = "inventory_2")

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(id: String): DeleteCategoryResult
}

sealed interface DeleteCategoryResult {
    data object Deleted : DeleteCategoryResult

    data class Blocked(val productCount: Int) : DeleteCategoryResult
}
