package com.example.krations.inventory.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Upsert
import androidx.room3.Update
import com.example.krations.inventory.data.local.entity.InventoryBatchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InventoryBatchDao {
    @Query(
        "SELECT * FROM InventoryBatchEntity " +
            "WHERE deletedAt IS NULL AND ownerUserId = :ownerUserId AND productId = :productId " +
            "ORDER BY expirationDate ASC",
    )
    fun getBatchesForProduct(productId: String, ownerUserId: String): Flow<List<InventoryBatchEntity>>

    @Query(
        "SELECT * FROM InventoryBatchEntity " +
            "WHERE id = :id AND ownerUserId = :ownerUserId AND deletedAt IS NULL",
    )
    suspend fun getBatchById(id: String, ownerUserId: String): InventoryBatchEntity?

    @Query("SELECT * FROM InventoryBatchEntity WHERE ownerUserId = :ownerUserId")
    suspend fun getRowsForSync(ownerUserId: String): List<InventoryBatchEntity>

    @Upsert
    suspend fun upsertRowsForSync(rows: List<InventoryBatchEntity>)

    @Insert
    suspend fun insertBatch(batch: InventoryBatchEntity)

    @Update
    suspend fun updateBatch(batch: InventoryBatchEntity)

    @Query(
        "UPDATE InventoryBatchEntity SET quantity = quantity + :delta, updatedAt = :updatedAt " +
            "WHERE id = (" +
            "SELECT id FROM InventoryBatchEntity " +
            "WHERE productId = :productId AND ownerUserId = :ownerUserId AND deletedAt IS NULL " +
            "AND expirationDate IS :expirationDate " +
            "ORDER BY createdAt ASC LIMIT 1)",
    )
    suspend fun addToBatchQuantity(
        delta: Double,
        updatedAt: String,
        productId: String,
        ownerUserId: String,
        expirationDate: String?,
    ): Int

    @Query(
        "UPDATE InventoryBatchEntity SET deletedAt = :deletedAt, updatedAt = :updatedAt " +
            "WHERE id = :id AND ownerUserId = :ownerUserId",
    )
    suspend fun deleteBatch(deletedAt: String, updatedAt: String, id: String, ownerUserId: String)
}
