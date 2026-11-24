package com.example.krations.inventory.data

import com.example.krations.core.data.LOCAL_USER_ID
import com.example.krations.inventory.data.local.dao.InventoryBatchDao
import com.example.krations.inventory.data.local.dao.ProductDao
import com.example.krations.inventory.data.local.entity.InventoryBatchEntity
import com.example.krations.inventory.data.local.entity.ProductEntity
import com.example.krations.inventory.domain.AddQuantityResult
import com.example.krations.inventory.domain.InventoryBatchRepository
import com.example.krations.inventory.domain.model.InventoryBatch
import kotlin.time.Clock.System.now
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class InventoryBatchRepositoryImpl(
    private val batchDao: InventoryBatchDao,
    private val productDao: ProductDao,
) : InventoryBatchRepository {

    override fun observeBatches(productId: String): Flow<List<InventoryBatch>> =
        batchDao
            .getBatchesForProduct(productId, LOCAL_USER_ID)
            .map { rows -> rows.map(InventoryBatchEntity::toBatch) }

    override suspend fun addQuantity(
        productId: String,
        quantity: Double,
        expirationDate: String?,
    ): AddQuantityResult {
        require(quantity > 0.0) { "addQuantity needs a positive quantity, got $quantity" }

        if (productDao.getProductById(productId, LOCAL_USER_ID) == null) {
            return AddQuantityResult.UnknownProduct(productId)
        }

        val timestamp = now().toString()
        addToStock(productId, quantity, expirationDate, timestamp)
        return AddQuantityResult.Added(productId)
    }

    override suspend fun addQuantity(
        productName: String,
        productUnit: String,
        quantity: Double,
        expirationDate: String?,
    ): AddQuantityResult {
        require(quantity > 0.0) { "addQuantity needs a positive quantity, got $quantity" }

        val name = productName.trim()
        require(name.isNotEmpty()) { "addQuantity needs a product name, got \"$productName\"" }

        val timestamp = now().toString()
        val existing = productDao.getProductByName(name, LOCAL_USER_ID)
        if (existing != null) {
            addToStock(existing.id, quantity, expirationDate, timestamp)
            return AddQuantityResult.Added(existing.id)
        }

        val productId = Uuid.random().toString()
        productDao.insertProduct(
            ProductEntity(
                id = productId,
                ownerUserId = LOCAL_USER_ID,
                categoryId = null,
                name = name,
                description = null,
                iconName = null,
                unit = productUnit.trim().ifEmpty { "unit" },
                createdAt = timestamp,
                updatedAt = timestamp,
                deletedAt = null,
            ),
        )
        addToStock(productId, quantity, expirationDate, timestamp)
        return AddQuantityResult.Created(productId)
    }

    override suspend fun addBatch(productId: String, quantity: Double, expirationDate: String?) {
        require(quantity > 0.0) { "addBatch needs a positive quantity, got $quantity" }

        if (productDao.getProductById(productId, LOCAL_USER_ID) == null) return

        insertBatch(productId, quantity, expirationDate, now().toString())
    }

    override suspend fun setQuantity(batchId: String, quantity: Double) {
        require(quantity >= 0.0) { "setQuantity needs a quantity not lower than zero, got $quantity" }

        val existing = batchDao.getBatchById(batchId, LOCAL_USER_ID) ?: return
        batchDao.updateBatch(existing.copy(quantity = quantity, updatedAt = now().toString()))
    }

    override suspend fun deleteBatch(batchId: String) = now().toString().let { timestamp ->
        batchDao.deleteBatch(
            deletedAt = timestamp,
            updatedAt = timestamp,
            id = batchId,
            ownerUserId = LOCAL_USER_ID,
        )
    }

    private suspend fun addToStock(
        productId: String,
        quantity: Double,
        expirationDate: String?,
        timestamp: String,
    ) {
        val incremented = batchDao.addToBatchQuantity(
            delta = quantity,
            updatedAt = timestamp,
            productId = productId,
            ownerUserId = LOCAL_USER_ID,
            expirationDate = expirationDate,
        )
        if (incremented == 0) insertBatch(productId, quantity, expirationDate, timestamp)
    }

    private suspend fun insertBatch(
        productId: String,
        quantity: Double,
        expirationDate: String?,
        timestamp: String,
    ) {
        batchDao.insertBatch(
            InventoryBatchEntity(
                id = Uuid.random().toString(),
                ownerUserId = LOCAL_USER_ID,
                productId = productId,
                quantity = quantity,
                expirationDate = expirationDate,
                createdAt = timestamp,
                updatedAt = timestamp,
                deletedAt = null,
            ),
        )
    }
}
