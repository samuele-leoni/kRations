package com.example.krations.inventory.data.repository

import com.example.krations.inventory.data.local.dao.ProductDao
import com.example.krations.inventory.data.local.entity.ProductEntity
import kotlin.time.Clock
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImpl(
    private val productDao: ProductDao,
) : ProductRepository {

    override fun getProducts(query: String, categoryId: String?): Flow<List<ProductEntity>> =
        productDao.searchProducts(query.trim(), categoryId)

    override suspend fun getProductById(id: String): ProductEntity? =
        productDao.getProductById(id)

    override suspend fun insertProduct(product: ProductEntity) =
        productDao.insertProduct(product)

    override suspend fun updateProduct(product: ProductEntity) =
        productDao.updateProduct(product)

    override suspend fun deleteProduct(id: String) {
        val now = Clock.System.now().toString()
        productDao.deleteProduct(deletedAt = now, updatedAt = now, id = id)
    }
}
