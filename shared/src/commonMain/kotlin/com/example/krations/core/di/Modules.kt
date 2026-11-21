package com.example.krations.core.di

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.krations.AuthViewModel
import com.example.krations.core.config.BuildKonfig
import com.example.krations.core.database.AppDatabase
import com.example.krations.shoppinglist.ui.ShoppingListViewModel
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.Module
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
}

val viewModelModule = module {
    viewModelOf(::ShoppingListViewModel)
    viewModel { AuthViewModel(get()) }
}

// Dipendenze specifiche per piattaforma
expect val platformModule: Module
