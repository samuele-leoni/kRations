package com.example.krations.inventory.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.krations.inventory.data.local.entity.ProductEntity
import com.example.krations.inventory.data.local.entity.ProductWithStock
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query(
        "SELECT p.*, COALESCE(SUM(b.quantity), 0) AS totalQuantity " +
            "FROM ProductEntity p " +
            "LEFT JOIN InventoryBatchEntity b ON b.productId = p.id AND b.ownerUserId = p.ownerUserId " +
            "AND b.deletedAt IS NULL " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.ownerUserId = :ownerUserId " +
            "AND (:noCategoryFilter = 1 OR p.categoryId IN (:categoryIds) " +
            "OR (:includeUncategorized = 1 AND p.categoryId IS NULL)) " +
            "AND (:query = '' OR p.name LIKE '%' || :query || '%' ESCAPE '\\' " +
            "OR p.description LIKE '%' || :query || '%' ESCAPE '\\') " +
            "GROUP BY p.id " +
            "ORDER BY p.name COLLATE NOCASE ASC",
    )
    fun searchProducts(
        query: String,
        categoryIds: List<String>,
        includeUncategorized: Boolean,
        noCategoryFilter: Boolean,
        ownerUserId: String,
    ): Flow<List<ProductWithStock>>

    @Query(
        "SELECT p.*, COALESCE(SUM(b.quantity), 0) AS totalQuantity " +
            "FROM ProductEntity p " +
            "LEFT JOIN InventoryBatchEntity b ON b.productId = p.id AND b.ownerUserId = p.ownerUserId " +
            "AND b.deletedAt IS NULL " +
            "WHERE p.deletedAt IS NULL " +
            "AND p.id = :id " +
            "AND p.ownerUserId = :ownerUserId " +
            "GROUP BY p.id",
    )
    suspend fun getProductById(id: String, ownerUserId: String): ProductWithStock?

    @Query(
        "SELECT * FROM ProductEntity " +
            "WHERE ownerUserId = :ownerUserId AND deletedAt IS NULL " +
            "AND TRIM(name) = TRIM(:name) COLLATE NOCASE " +
            "ORDER BY createdAt ASC LIMIT 1",
    )
    suspend fun getProductByName(name: String, ownerUserId: String): ProductEntity?

    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query(
        "UPDATE ProductEntity SET deletedAt = :deletedAt, updatedAt = :updatedAt " +
            "WHERE id = :id AND ownerUserId = :ownerUserId",
    )
    suspend fun deleteProduct(deletedAt: String, updatedAt: String, id: String, ownerUserId: String)
}
