package com.example.krations.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.example.krations.shoppinglist.data.local.dao.ShoppingListItemDao
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItem

@Database(entities = [ShoppingListItem::class], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListItemDao(): ShoppingListItemDao
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}