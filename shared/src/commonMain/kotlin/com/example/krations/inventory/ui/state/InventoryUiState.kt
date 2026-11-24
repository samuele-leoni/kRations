package com.example.krations.inventory.ui.state

import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.domain.model.Product

data class InventoryUiState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val searchQuery: String = "",
    val selectedCategoryIds: Set<String> = emptySet(),
    val uncategorizedSelected: Boolean = false,
) {
    val hasActiveFilters: Boolean
        get() = searchQuery.isNotBlank() || selectedCategoryIds.isNotEmpty() || uncategorizedSelected
}
