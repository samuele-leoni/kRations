package com.example.krations.inventory.ui

import com.example.krations.core.ui.utils.formatQuantity
import com.example.krations.inventory.domain.model.Product

internal const val DEFAULT_UNIT = "unit"

internal fun formatStock(product: Product): String = formatStock(product.stock, product.unit)

internal fun formatStock(quantity: Double, unit: String): String =
    "${quantity.formatQuantity()} ${formatUnit(quantity, unit)}"

private fun formatUnit(quantity: Double, unit: String): String =
    if (unit == DEFAULT_UNIT && quantity.formatQuantity() != "1") "${unit}s" else unit
