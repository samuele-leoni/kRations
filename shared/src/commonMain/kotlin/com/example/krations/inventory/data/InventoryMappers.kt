package com.example.krations.inventory.data

import com.example.krations.inventory.data.local.entity.CategoryEntity
import com.example.krations.inventory.data.local.entity.CategoryWithCount
import com.example.krations.inventory.data.local.entity.InventoryBatchEntity
import com.example.krations.inventory.data.local.entity.ProductWithStock
import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.domain.model.CategorySummary
import com.example.krations.inventory.domain.model.InventoryBatch
import com.example.krations.inventory.domain.model.Product

fun ProductWithStock.toProduct(): Product = Product(
    id = product.id,
    name = product.name,
    description = product.description,
    unit = product.unit,
    iconName = product.iconName,
    categoryId = product.categoryId,
    minQuantity = product.minQuantity,
    expiryAlertDays = product.expiryAlertDays,
    stock = totalQuantity,
)

fun CategoryEntity.toCategory(): Category = Category(
    id = id,
    name = name,
    iconName = iconName,
)

fun CategoryWithCount.toSummary(): CategorySummary =
    CategorySummary(
        category = category.toCategory(),
        productCount = productCount.toInt(),
    )

fun InventoryBatchEntity.toBatch(): InventoryBatch = InventoryBatch(
    id = id,
    productId = productId,
    quantity = quantity,
    expirationDate = expirationDate,
)
