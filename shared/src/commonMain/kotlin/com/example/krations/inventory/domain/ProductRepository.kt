package com.example.krations.inventory.domain

import com.example.krations.inventory.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(query: String, categoryId: String?): Flow<List<Product>>

    suspend fun addProduct(name: String, unit: String = "unit", categoryId: String? = null)

    suspend fun updateProduct(product: Product)

    suspend fun deleteProduct(id: String)
}
