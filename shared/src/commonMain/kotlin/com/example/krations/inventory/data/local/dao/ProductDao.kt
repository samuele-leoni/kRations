package com.example.krations.inventory.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.krations.inventory.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM ProductEntity WHERE deletedAt IS NULL")
    fun getProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM ProductEntity WHERE id = :id AND deletedAt IS NULL")
    suspend fun getProductById(id: String): ProductEntity?

    @Insert
    suspend fun insertProduct(product: ProductEntity)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("UPDATE ProductEntity SET deletedAt = :deletedAt, updatedAt = :updatedAt WHERE id = :id")
    suspend fun deleteProduct(deletedAt: String, updatedAt: String, id: String)
}
