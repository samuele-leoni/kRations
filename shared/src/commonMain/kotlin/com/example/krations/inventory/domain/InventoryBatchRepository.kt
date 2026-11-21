package com.example.krations.inventory.domain

import com.example.krations.inventory.domain.model.InventoryBatch
import kotlinx.coroutines.flow.Flow

interface InventoryBatchRepository {
    fun observeBatches(productId: String): Flow<List<InventoryBatch>>

    suspend fun addQuantity(
        productId: String,
        quantity: Double,
        expirationDate: String? = null,
    ): AddQuantityResult

    suspend fun addQuantity(
        productName: String,
        productUnit: String,
        quantity: Double,
        expirationDate: String? = null,
    ): AddQuantityResult

    suspend fun addBatch(productId: String, quantity: Double, expirationDate: String? = null)

    suspend fun setQuantity(batchId: String, quantity: Double)

    suspend fun deleteBatch(batchId: String)
}

sealed interface AddQuantityResult {
    data class Added(val productId: String) : AddQuantityResult

    data class Created(val productId: String) : AddQuantityResult

    data class UnknownProduct(val productId: String) : AddQuantityResult
}
