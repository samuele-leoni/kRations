package com.example.krations.core.di

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.krations.alerts.data.AlarmeeExpirationNotifier
import com.example.krations.alerts.data.FakeExpiringItemsRepository
import com.example.krations.alerts.domain.ExpirationNotifier
import com.example.krations.alerts.domain.ExpiringItemsRepository
import com.example.krations.alerts.domain.RescheduleExpirationRemindersUseCase
import com.example.krations.core.database.AppDatabase
import com.example.krations.inventory.ui.InventoryViewModel
import com.example.krations.inventory.ui.category.CategoryEditViewModel
import com.example.krations.inventory.ui.category.CategoryListViewModel
import com.example.krations.inventory.ui.product.ProductEditViewModel
import com.example.krations.shoppinglist.ui.ShoppingListViewModel
import com.tweener.alarmee.createAlarmeeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
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
    viewModelOf(::CategoryListViewModel)
    viewModelOf(::CategoryEditViewModel)
    viewModelOf(::ShoppingListViewModel)
}

val alertsModule = module {
    single { createAlarmeeService().apply { initialize(get()) } }
    single<ExpiringItemsRepository> { FakeExpiringItemsRepository() }
    single<ExpirationNotifier> { AlarmeeExpirationNotifier(get()) }
    factoryOf(::RescheduleExpirationRemindersUseCase)
}

// Platform specific dependencies
expect val platformModule: Module
