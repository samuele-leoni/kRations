package com.example.krations.inventory.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
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

    @Insert
    suspend fun insertBatch(batch: InventoryBatchEntity)

    @Update
    suspend fun updateBatch(batch: InventoryBatchEntity)

    @Query(
        "UPDATE InventoryBatchEntity SET deletedAt = :deletedAt, updatedAt = :updatedAt " +
            "WHERE id = :id AND ownerUserId = :ownerUserId",
    )
    suspend fun deleteBatch(deletedAt: String, updatedAt: String, id: String, ownerUserId: String)
}
