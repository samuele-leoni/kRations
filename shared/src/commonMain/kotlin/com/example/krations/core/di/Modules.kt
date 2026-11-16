package com.example.krations.core.di

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.krations.core.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<AppDatabase>().shoppingListItemDao() }
}

val viewModelModule = module {
    //viewModelOf(::ShoppingListViewModel)
}

// Dipendenze specifiche per piattaforma
expect val platformModule: Module