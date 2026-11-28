package com.example.krations.inventory.di

import com.example.krations.inventory.data.CategoryRepositoryImpl
import com.example.krations.inventory.data.InventoryBatchRepositoryImpl
import com.example.krations.inventory.data.ProductRepositoryImpl
import com.example.krations.inventory.domain.CategoryRepository
import com.example.krations.inventory.domain.InventoryBatchRepository
import com.example.krations.inventory.domain.ProductRepository
import org.koin.dsl.module

val inventoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get(), get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get(), get()) }
    single<InventoryBatchRepository> { InventoryBatchRepositoryImpl(get(), get(), get()) }
}
