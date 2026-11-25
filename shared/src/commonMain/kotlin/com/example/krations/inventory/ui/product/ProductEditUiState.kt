package com.example.krations.inventory.ui.product

import com.example.krations.inventory.domain.model.Category

val productUnits = listOf(
    "Liters",
    "Milliliters",
    "Grams",
    "Kilograms",
    "Pieces",
    "Boxes",
    "Bottles",
    "Cans",
    "Packs",
    "Dozen",
)

data class BatchDraft(
    val batchId: String? = null,
    val quantity: Int = 1,
    val expirationDate: String? = null,
)

data class ProductEditUiState(
    val isCreate: Boolean = true,
    val name: String = "",
    val unit: String = productUnits.first(),
    val categoryId: String? = null,
    val minQuantity: Double = 0.0,
    val expiryAlertDays: Int = 7,
    val categories: List<Category> = emptyList(),
    val batches: List<BatchDraft> = emptyList(),
    val isLoading: Boolean = false,
) {
    val canSave: Boolean get() = name.isNotBlank() && !isLoading
}
