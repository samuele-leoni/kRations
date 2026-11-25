package com.example.krations.alerts.data

import com.example.krations.alerts.domain.LowStockItemsRepository
import com.example.krations.alerts.domain.model.LowStockItem
import com.example.krations.inventory.domain.ProductRepository
import com.example.krations.inventory.domain.model.needsRestock
import com.example.krations.inventory.domain.model.stockStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductLowStockItemsRepository(
    private val productRepository: ProductRepository,
) : LowStockItemsRepository {

    override fun observeItems(): Flow<List<LowStockItem>> =
        productRepository.observeAllProducts()
            .map { products ->
                products
                    .filter { it.minQuantity > 0.0 && it.stockStatus().needsRestock }
                    .map { product ->
                        LowStockItem(
                            productName = product.name,
                            unit = product.unit,
                            stock = product.stock,
                            minQuantity = product.minQuantity,
                        )
                    }
            }
}
