package com.example.krations.alerts.data

import com.example.krations.alerts.domain.ExpiringItemsRepository
import com.example.krations.alerts.domain.model.ExpiringItem
import com.example.krations.inventory.domain.InventoryBatchRepository
import com.example.krations.inventory.domain.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate

class ExpiringItemsRepositoryImpl(
    private val productRepository: ProductRepository,
    private val inventoryBatchRepository: InventoryBatchRepository,
) : ExpiringItemsRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeItems(): Flow<List<ExpiringItem>> =
        productRepository.observeAllProducts()
            .flatMapLatest { products ->
                if (products.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(
                        products.map { product ->
                            inventoryBatchRepository.observeBatches(product.id)
                                .map { batches ->
                                    batches.mapNotNull { batch ->
                                        batch.expirationDate?.let { date ->
                                            ExpiringItem(
                                                productName = product.name,
                                                batchId = batch.id,
                                                expirationDate = LocalDate.parse(date),
                                                expiryAlertDays = product.expiryAlertDays,
                                            )
                                        }
                                    }
                                }
                        },
                    ) { itemsByProduct ->
                        itemsByProduct.flatMap { it }
                    }
                }
            }
}