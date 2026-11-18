package com.example.krations.shoppinglist.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListItemDao {
    @Query("SELECT * FROM ShoppingListItem WHERE deletedAt IS NULL ORDER BY createdAt DESC")
    fun getShoppingListItems(): Flow<List<ShoppingListItemEntity>>

    @Query("SELECT * FROM ShoppingListItem WHERE id = :id AND deletedAt IS NULL")
    suspend fun getItemById(id: String): ShoppingListItemEntity?

    @Insert
    suspend fun insertItem(item: ShoppingListItemEntity)

    @Update
    suspend fun updateItem(item: ShoppingListItemEntity)

    @Query("UPDATE ShoppingListItem SET deletedAt = :deletedAt, updatedAt = :updatedAt WHERE id = :id")
    suspend fun deleteItem(deletedAt: String, updatedAt: String, id: String)

    @Query("UPDATE ShoppingListItem SET deletedAt = :timestamp, updatedAt = :timestamp WHERE isPurchased = 1 AND deletedAt IS NULL")
    suspend fun deletePurchasedItems(timestamp: String)
}