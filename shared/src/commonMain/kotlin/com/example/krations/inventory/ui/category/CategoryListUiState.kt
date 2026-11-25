package com.example.krations.inventory.ui.category

import com.example.krations.inventory.domain.model.CategorySummary

data class CategoryListUiState(
    val categories: List<CategorySummary> = emptyList(),
    val notice: String? = null,
)
