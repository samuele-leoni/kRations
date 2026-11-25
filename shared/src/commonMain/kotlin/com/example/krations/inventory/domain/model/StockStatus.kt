package com.example.krations.inventory.domain.model

enum class StockStatus(val label: String) {
    InStock("In Stock"),
    LowStock("Low\nStock"),
    OutOfStock("Out of\nStock"),
}

fun Product.stockStatus(): StockStatus = when {
    stock <= 0.0 -> StockStatus.OutOfStock
    stock < minQuantity -> StockStatus.LowStock
    else -> StockStatus.InStock
}

val StockStatus.needsRestock: Boolean
    get() = this != StockStatus.InStock
