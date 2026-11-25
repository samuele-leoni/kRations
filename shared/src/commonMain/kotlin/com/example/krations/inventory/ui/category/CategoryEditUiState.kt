package com.example.krations.inventory.ui.category

import com.example.krations.inventory.ui.common.defaultIconName

data class CategoryEditUiState(
    val isCreate: Boolean = true,
    val isLoading: Boolean = false,
    val name: String = "",
    val iconName: String = defaultIconName,
    val deleteBlockedCount: Int? = null,
) {
    val canSave: Boolean get() = name.isNotBlank() && !isLoading
}
