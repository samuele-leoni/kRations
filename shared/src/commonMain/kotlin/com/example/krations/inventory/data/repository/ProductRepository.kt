package com.example.krations.inventory.data.repository

import com.example.krations.inventory.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(query: String, categoryId: String?): Flow<List<ProductEntity>>

    suspend fun getProductById(id: String): ProductEntity?

    suspend fun insertProduct(product: ProductEntity)

    suspend fun updateProduct(product: ProductEntity)

    suspend fun deleteProduct(id: String)
}
