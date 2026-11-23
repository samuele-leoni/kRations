package com.example.krations.core.di

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.krations.core.database.AppDatabase
import com.example.krations.inventory.ui.InventoryViewModel
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
            .fallbackToDestructiveMigration()
            .build()
    }
    single { get<AppDatabase>().shoppingListItemDao() }
    single { get<AppDatabase>().productDao() }
    single { get<AppDatabase>().categoryDao() }
    single { get<AppDatabase>().inventoryBatchDao() }
}

val viewModelModule = module {
    viewModelOf(::InventoryViewModel)
    viewModelOf(::ShoppingListViewModel)
}

// Dipendenze specifiche per piattaforma
expect val platformModule: Module
