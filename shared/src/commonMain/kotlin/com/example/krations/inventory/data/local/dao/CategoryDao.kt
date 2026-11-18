package com.example.krations.inventory.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.krations.inventory.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM CategoryEntity WHERE deletedAt IS NULL ORDER BY name COLLATE NOCASE ASC")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM CategoryEntity WHERE id = :id AND deletedAt IS NULL")
    suspend fun getCategoryById(id: String): CategoryEntity?

    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("UPDATE CategoryEntity SET deletedAt = :deletedAt, updatedAt = :updatedAt WHERE id = :id")
    suspend fun deleteCategory(deletedAt: String, updatedAt: String, id: String)
}
