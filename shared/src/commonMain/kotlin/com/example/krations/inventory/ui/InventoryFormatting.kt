package com.example.krations.inventory.ui

import com.example.krations.inventory.domain.model.Product

internal const val DEFAULT_UNIT = "unit"

internal fun formatStock(product: Product): String = formatStock(product.stock, product.unit)

internal fun formatStock(quantity: Double, unit: String): String =
    "${formatQuantity(quantity)} ${formatUnit(quantity, unit)}"

internal fun formatQuantity(quantity: Double): String =
    if (quantity % 1.0 == 0.0) quantity.toInt().toString() else quantity.toString()

private fun formatUnit(quantity: Double, unit: String): String =
    if (unit == DEFAULT_UNIT && formatQuantity(quantity) != "1") "${unit}s" else unit
