package com.example.krations.core.database

import androidx.room3.AutoMigration
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.example.krations.inventory.data.local.dao.ProductDao
import com.example.krations.inventory.data.local.entity.ProductEntity
import com.example.krations.shoppinglist.data.local.dao.ShoppingListItemDao
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItem

@Database(
    entities = [ShoppingListItem::class, ProductEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)],
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListItemDao(): ShoppingListItemDao

    abstract fun productDao(): ProductDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}
