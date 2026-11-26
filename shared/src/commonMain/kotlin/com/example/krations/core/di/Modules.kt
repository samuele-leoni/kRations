package com.example.krations.core.di

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.krations.AuthViewModel
import com.example.krations.alerts.data.AlarmeeExpirationNotifier
import com.example.krations.alerts.data.AlarmeeLowStockNotifier
import com.example.krations.alerts.data.ExpiringItemsRepositoryImpl
import com.example.krations.alerts.data.ProductLowStockItemsRepository
import com.example.krations.alerts.domain.ExpirationNotifier
import com.example.krations.alerts.domain.ExpiringItemsRepository
import com.example.krations.alerts.domain.LowStockItemsRepository
import com.example.krations.alerts.domain.LowStockNotifier
import com.example.krations.alerts.domain.RescheduleExpirationRemindersUseCase
import com.example.krations.alerts.domain.RescheduleLowStockRemindersUseCase
import com.example.krations.core.config.BuildKonfig
import com.example.krations.core.database.AppDatabase
import com.example.krations.inventory.ui.InventoryViewModel
import com.example.krations.inventory.ui.category.CategoryEditViewModel
import com.example.krations.inventory.ui.category.CategoryListViewModel
import com.example.krations.inventory.ui.product.ProductEditViewModel
import com.tweener.alarmee.createAlarmeeService
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val dataModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = BuildKonfig.SUPABASE_URL,
            supabaseKey = BuildKonfig.SUPABASE_PUBLISHABLE_KEY,
        ) {
            install(Auth) {
                scheme = "com.example.krations"
                host = "auth"
            }
            install(Postgrest)
        }
    }
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
    viewModel { AuthViewModel(get()) }
}

val alertsModule = module {
    single { createAlarmeeService().apply { initialize(get()) } }
    single<ExpiringItemsRepository> { ExpiringItemsRepositoryImpl(get(), get()) }
    single<ExpirationNotifier> { AlarmeeExpirationNotifier(get()) }
    factoryOf(::RescheduleExpirationRemindersUseCase)
    single<LowStockItemsRepository> { ProductLowStockItemsRepository(get()) }
    single<LowStockNotifier> { AlarmeeLowStockNotifier(get()) }
    factoryOf(::RescheduleLowStockRemindersUseCase)
}

// Platform specific dependencies
expect val platformModule: Module
