package com.example.krations.core.di

import android.app.NotificationManager
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.example.krations.alerts.domain.EXPIRATION_CHANNEL_ID
import com.example.krations.alerts.domain.LOW_STOCK_CHANNEL_ID
import com.example.krations.core.database.AppDatabase
import com.tweener.alarmee.channel.AlarmeeNotificationChannel
import com.tweener.alarmee.configuration.AlarmeeAndroidPlatformConfiguration
import com.tweener.alarmee.configuration.AlarmeePlatformConfiguration
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

    single<AlarmeePlatformConfiguration> {
        AlarmeeAndroidPlatformConfiguration(
            notificationIconResId = android.R.drawable.ic_dialog_info,
            notificationChannels = listOf(
                AlarmeeNotificationChannel(
                    id = EXPIRATION_CHANNEL_ID,
                    name = "Expiration reminders",
                    importance = NotificationManager.IMPORTANCE_DEFAULT,
                ),
                AlarmeeNotificationChannel(
                    id = LOW_STOCK_CHANNEL_ID,
                    name = "Low stock reminders",
                    importance = NotificationManager.IMPORTANCE_DEFAULT,
                ),
            ),
        )
    }
}