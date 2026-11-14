package com.example.krations

import android.app.Application
import com.example.krations.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class KRationsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@KRationsApp)
        }
    }
}