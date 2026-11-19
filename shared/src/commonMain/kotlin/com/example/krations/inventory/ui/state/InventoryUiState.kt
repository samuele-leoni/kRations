package com.example.krations.inventory.ui.state

import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.domain.model.Product

data class InventoryUiState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val selectedCategoryId: String? = null,
)
