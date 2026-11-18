package com.example.krations.shoppinglist.data

import com.example.krations.shoppinglist.data.local.entity.ShoppingListItemEntity
import com.example.krations.shoppinglist.domain.model.ShoppingListItem

fun ShoppingListItemEntity.toShoppingListItem(): ShoppingListItem = ShoppingListItem(
    id = id,
    productId = productId,
    name = name,
    quantityNeeded = quantityNeeded,
    unit = unit,
    isPurchased = isPurchased,
    purchasedQuantity = purchasedQuantity,
    purchasedAt = purchasedAt,
)