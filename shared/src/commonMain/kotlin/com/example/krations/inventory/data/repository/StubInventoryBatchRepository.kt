package com.example.krations.inventory.data.repository

import co.touchlab.kermit.Logger

class StubInventoryBatchRepository {
    private val log = Logger.withTag("InventoryStub")

    suspend fun addQuantity(productId: String, quantity: Double, expirationDate: String? = null) {
        log.d { "addQuantity: $quantity of $productId, expires $expirationDate" }
    }
}