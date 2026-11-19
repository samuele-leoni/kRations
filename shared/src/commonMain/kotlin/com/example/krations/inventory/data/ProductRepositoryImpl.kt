package com.example.krations.inventory.data

import com.example.krations.inventory.data.local.dao.ProductDao
import com.example.krations.inventory.data.local.entity.ProductEntity
import com.example.krations.inventory.data.local.entity.ProductWithStock
import com.example.krations.inventory.domain.ProductRepository
import com.example.krations.inventory.domain.model.Product
import kotlin.time.Clock.System.now
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao,
) : ProductRepository {

    override fun observeProducts(query: String, categoryId: String?): Flow<List<Product>> =
        productDao.searchProducts(query.trim(), categoryId, LOCAL_USER_ID).map { rows ->
            rows.map(ProductWithStock::toProduct)
        }

    override suspend fun addProduct(name: String, unit: String, categoryId: String?) =
        now().toString().let { timestamp ->
            productDao.insertProduct(
                ProductEntity(
                    id = Uuid.random().toString(),
                    ownerUserId = LOCAL_USER_ID,
                    categoryId = categoryId,
                    name = name,
                    description = null,
                    iconName = null,
                    unit = unit,
                    minQuantity = 0.0,
                    createdAt = timestamp,
                    updatedAt = timestamp,
                    deletedAt = null,
                ),
            )
        }

    override suspend fun updateProduct(product: Product) {
        val existing = productDao.getProductById(product.id, LOCAL_USER_ID) ?: return
        productDao.updateProduct(
            existing.copy(
                categoryId = product.categoryId,
                name = product.name,
                description = product.description,
                iconName = product.iconName,
                unit = product.unit,
                minQuantity = product.minQuantity,
                updatedAt = now().toString(),
            ),
        )
    }

    override suspend fun deleteProduct(id: String) = now().toString().let { timestamp ->
        productDao.deleteProduct(
            deletedAt = timestamp,
            updatedAt = timestamp,
            id = id,
            ownerUserId = LOCAL_USER_ID,
        )
    }
}

// TODO: get real user from account data
private const val LOCAL_USER_ID = "local-user"
