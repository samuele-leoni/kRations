package com.example.krations.inventory.data

import com.example.krations.core.data.LOCAL_USER_ID
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

    override fun observeProducts(
        query: String,
        categoryIds: Set<String>,
        includeUncategorized: Boolean,
    ): Flow<List<Product>> {
        val ids = categoryIds.toList().ifEmpty { listOf(NO_CATEGORY_SENTINEL) }
        val noCategoryFilter = categoryIds.isEmpty() && !includeUncategorized
        return productDao
            .searchProducts(query.trim(), ids, includeUncategorized, noCategoryFilter, LOCAL_USER_ID)
            .map { rows -> rows.map(ProductWithStock::toProduct) }
    }

    override suspend fun getProduct(id: String): Product? =
        productDao.getProductById(id, LOCAL_USER_ID)?.toProduct()

    override suspend fun addProduct(
        name: String,
        description: String?,
        unit: String,
        categoryId: String?,
        minQuantity: Double,
        expiryAlertDays: Int,
    ): String {
        val id = Uuid.random().toString()
        now().toString().let { timestamp ->
            productDao.insertProduct(
                ProductEntity(
                    id = id,
                    ownerUserId = LOCAL_USER_ID,
                    categoryId = categoryId,
                    name = name,
                    description = description,
                    iconName = null,
                    unit = unit,
                    minQuantity = minQuantity,
                    expiryAlertDays = expiryAlertDays,
                    createdAt = timestamp,
                    updatedAt = timestamp,
                    deletedAt = null,
                ),
            )
        }
        return id
    }

    override suspend fun updateProduct(product: Product) {
        val existing = productDao.getProductById(product.id, LOCAL_USER_ID)?.product ?: return
        productDao.updateProduct(
            existing.copy(
                categoryId = product.categoryId,
                name = product.name,
                description = product.description,
                iconName = product.iconName,
                unit = product.unit,
                minQuantity = product.minQuantity,
                expiryAlertDays = product.expiryAlertDays,
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

    private companion object {
        private const val NO_CATEGORY_SENTINEL = "no-category-selected"
    }
}
