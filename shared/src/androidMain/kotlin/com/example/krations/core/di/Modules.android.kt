package com.example.krations.core.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.krations.core.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val context = androidContext()
        val dbFile = context.getDatabasePath("krations.db")
        Room.databaseBuilder<AppDatabase>(
            context = context,
            name = dbFile.absolutePath,
        )
    }
}