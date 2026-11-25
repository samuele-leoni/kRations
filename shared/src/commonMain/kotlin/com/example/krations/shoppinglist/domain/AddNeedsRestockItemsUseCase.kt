package com.example.krations.shoppinglist.domain

import com.example.krations.inventory.domain.ProductRepository
import com.example.krations.inventory.domain.model.Product
import com.example.krations.inventory.domain.model.needsRestock
import com.example.krations.inventory.domain.model.stockStatus
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import kotlinx.coroutines.flow.combine

class AddNeedsRestockItemsUseCase(
    private val productRepository: ProductRepository,
    private val shoppingListRepository: ShoppingListRepository,
) {

    suspend operator fun invoke() {
        combine(
            productRepository.observeAllProducts(),
            shoppingListRepository.observeItems(),
        ) { products, items ->
            products
                .filter { it.stockStatus().needsRestock }
                .filterNot { product -> items.any { item -> item.isAbout(product) } }
        }
            .collect { missing ->
                missing.forEach { product ->
                    shoppingListRepository.addItem(
                        name = product.name,
                        quantityNeeded = product.minQuantity + 1,
                        unit = product.unit,
                        productId = product.id,
                    )
                }
            }
    }

    private fun ShoppingListItem.isAbout(product: Product): Boolean =
        (productId == product.id) ||
            (name.trim().equals(other = product.name.trim(), ignoreCase = true))
}
