package com.example.krations.shoppinglist.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Upsert
import androidx.room3.Update
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListItemDao {
    @Query("SELECT * FROM ShoppingListItem WHERE ownerUserId = :ownerUserId AND deletedAt IS NULL ORDER BY createdAt DESC")
    fun getShoppingListItems(ownerUserId: String): Flow<List<ShoppingListItemEntity>>

    @Query("SELECT * FROM ShoppingListItem WHERE id = :id AND ownerUserId = :ownerUserId AND deletedAt IS NULL")
    suspend fun getItemById(id: String, ownerUserId: String): ShoppingListItemEntity?

    @Query("SELECT * FROM ShoppingListItem WHERE ownerUserId = :ownerUserId")
    suspend fun getRowsForSync(ownerUserId: String): List<ShoppingListItemEntity>

    @Upsert
    suspend fun upsertRowsForSync(rows: List<ShoppingListItemEntity>)

    @Insert
    suspend fun insertItem(item: ShoppingListItemEntity)

    @Update
    suspend fun updateItem(item: ShoppingListItemEntity)

    @Query("UPDATE ShoppingListItem SET deletedAt = :deletedAt, updatedAt = :updatedAt WHERE id = :id AND ownerUserId = :ownerUserId")
    suspend fun deleteItem(deletedAt: String, updatedAt: String, id: String, ownerUserId: String)

    @Query("UPDATE ShoppingListItem SET deletedAt = :timestamp, updatedAt = :timestamp WHERE ownerUserId = :ownerUserId AND isPurchased = 1 AND deletedAt IS NULL")
    suspend fun deletePurchasedItems(timestamp: String, ownerUserId: String)

    @Query("UPDATE ShoppingListItem SET isPurchased = 1, purchasedAt = :timestamp, updatedAt = :timestamp WHERE id = :id AND ownerUserId = :ownerUserId AND deletedAt IS NULL")
    suspend fun setPurchased(id: String, timestamp: String, ownerUserId: String)

    @Query("UPDATE ShoppingListItem SET isPurchased = 1, purchasedAt = :timestamp, updatedAt = :timestamp WHERE ownerUserId = :ownerUserId AND deletedAt IS NULL")
    suspend fun setPurchasedAll(timestamp: String, ownerUserId: String)
}
