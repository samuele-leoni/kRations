package com.example.krations.inventory.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import com.example.krations.inventory.data.local.entity.CategoryEntity
import com.example.krations.inventory.data.local.entity.CategoryWithCount
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query(
        "SELECT * FROM CategoryEntity " +
            "WHERE deletedAt IS NULL AND ownerUserId = :ownerUserId " +
            "ORDER BY name COLLATE NOCASE ASC",
    )
    fun getCategories(ownerUserId: String): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM CategoryEntity WHERE id = :id AND ownerUserId = :ownerUserId AND deletedAt IS NULL")
    suspend fun getCategoryById(id: String, ownerUserId: String): CategoryEntity?

    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query(
        "SELECT c.*, COUNT(p.id) AS productCount FROM CategoryEntity c " +
            "LEFT JOIN ProductEntity p ON p.categoryId = c.id AND p.ownerUserId = c.ownerUserId " +
            "AND p.deletedAt IS NULL " +
            "WHERE c.deletedAt IS NULL AND c.ownerUserId = :ownerUserId " +
            "GROUP BY c.id " +
            "ORDER BY c.name COLLATE NOCASE ASC",
    )
    fun getCategoriesWithCount(ownerUserId: String): Flow<List<CategoryWithCount>>

    @Query(
        "SELECT COUNT(*) FROM ProductEntity " +
            "WHERE categoryId = :categoryId AND ownerUserId = :ownerUserId AND deletedAt IS NULL",
    )
    suspend fun countActiveProducts(categoryId: String, ownerUserId: String): Int

    @Transaction
    suspend fun softDeleteIfUnused(deletedAt: String, id: String, ownerUserId: String): Int {
        val attached = countActiveProducts(id, ownerUserId)
        if (attached > 0) return attached

        deleteCategory(deletedAt = deletedAt, updatedAt = deletedAt, id = id, ownerUserId = ownerUserId)
        return 0
    }

    @Query(
        "UPDATE CategoryEntity SET deletedAt = :deletedAt, updatedAt = :updatedAt " +
            "WHERE id = :id AND ownerUserId = :ownerUserId",
    )
    suspend fun deleteCategory(deletedAt: String, updatedAt: String, id: String, ownerUserId: String)
}
