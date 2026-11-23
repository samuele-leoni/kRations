package com.example.krations.core.di

import com.example.krations.inventory.di.inventoryModule
import com.example.krations.shoppinglist.di.shoppingListModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(dataModule, viewModelModule, platformModule, inventoryModule, shoppingListModule)
    }
}
