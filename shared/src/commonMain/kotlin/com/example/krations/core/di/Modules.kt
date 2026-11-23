package com.example.krations.core.di

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.krations.core.database.AppDatabase
import com.example.krations.inventory.ui.InventoryViewModel
import com.example.krations.inventory.ui.product.ProductEditViewModel
import com.example.krations.shoppinglist.ui.ShoppingListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            // The app is still in development and every local row is test data, so dropping and
            // rebuilding the tables is acceptable when the schema changes. Before the first release
            // this has to become a real migration: version 2 -> 3 drops ProductEntity.quantity,
            // makes CategoryEntity.iconName not null and turns the category foreign key into a
            // composite one, so a destructive migration would silently wipe user data.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<AppDatabase>().shoppingListItemDao() }
    single { get<AppDatabase>().productDao() }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().inventoryBatchDao() }
}

val viewModelModule = module {
    viewModelOf(::InventoryViewModel)
    viewModelOf(::ProductEditViewModel)
    viewModelOf(::ShoppingListViewModel)
}

// Dipendenze specifiche per piattaforma
expect val platformModule: Module
