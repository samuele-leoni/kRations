@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.example.krations.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.example.krations.inventory.data.local.dao.CategoryDao
import com.example.krations.inventory.data.local.dao.InventoryBatchDao
import com.example.krations.inventory.data.local.dao.ProductDao
import com.example.krations.inventory.data.local.entity.CategoryEntity
import com.example.krations.inventory.data.local.entity.InventoryBatchEntity
import com.example.krations.inventory.data.local.entity.ProductEntity
import com.example.krations.shoppinglist.data.local.dao.ShoppingListItemDao
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItem

@Database(
    entities = [
        ShoppingListItem::class,
        ProductEntity::class,
        CategoryEntity::class,
        InventoryBatchEntity::class,
    ],
    version = 3,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListItemDao(): ShoppingListItemDao

    abstract fun productDao(): ProductDao

    abstract fun categoryDao(): CategoryDao

    abstract fun inventoryBatchDao(): InventoryBatchDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
