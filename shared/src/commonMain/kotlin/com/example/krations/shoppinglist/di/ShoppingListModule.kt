package com.example.krations.shoppinglist.di

import com.example.krations.inventory.data.repository.StubInventoryBatchRepository
import com.example.krations.shoppinglist.data.ShoppingListRepositoryImpl
import com.example.krations.shoppinglist.domain.SetItemPurchasedUseCase
import com.example.krations.shoppinglist.domain.ShoppingListRepository
import com.example.krations.shoppinglist.ui.ShoppingListViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val shoppingListModule = module {
    single<ShoppingListRepository> { ShoppingListRepositoryImpl(get()) }
    viewModelOf(::ShoppingListViewModel)
    factoryOf(::SetItemPurchasedUseCase)
    single { StubInventoryBatchRepository() }
}