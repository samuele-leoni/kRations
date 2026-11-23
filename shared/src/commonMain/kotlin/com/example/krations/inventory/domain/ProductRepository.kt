package com.example.krations.inventory.domain

import com.example.krations.inventory.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(
        query: String,
        categoryIds: Set<String>,
        includeUncategorized: Boolean,
    ): Flow<List<Product>>

    suspend fun getProduct(id: String): Product?

    suspend fun addProduct(
        name: String,
        description: String?,
        unit: String,
        categoryId: String?,
        minQuantity: Double,
        expiryAlertDays: Int,
    ): String

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(id: String)
}
