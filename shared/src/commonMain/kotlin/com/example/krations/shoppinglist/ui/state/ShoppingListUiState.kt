package com.example.krations.shoppinglist.ui.state

import com.example.krations.shoppinglist.domain.model.ShoppingListItem

data class ShoppingListUiState (
    val items: List<ShoppingListItem> = emptyList(),
    val selectedTab: ShoppingListTab = ShoppingListTab.ToBuy,
    val searchQuery: String = "",
)