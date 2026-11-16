package com.example.krations.shoppinglist.data.local.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListItemDao {
    @Query("SELECT * FROM ShoppingListItem WHERE deletedAt IS NULL")
    fun getShoppingListItems(): Flow<List<ShoppingListItem>>

    @Query("SELECT * FROM ShoppingListItem WHERE id = :id AND deletedAt IS NULL")
    suspend fun getItemById(id: String): ShoppingListItem?

    @Insert
    suspend fun insertItem(item: ShoppingListItem)

    @Update
    suspend fun updateItem(item: ShoppingListItem)

    @Query("UPDATE ShoppingListItem SET deletedAt = :deletedAt, updatedAt = :updatedAt WHERE id = :id")
    suspend fun deleteItem(deletedAt: String, updatedAt: String, id: String)
}