package com.example.krations.shoppinglist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krations.shoppinglist.domain.ShoppingListRepository
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import com.example.krations.shoppinglist.ui.state.ShoppingListTab
import com.example.krations.shoppinglist.ui.state.ShoppingListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingListViewModel(
    private val repository: ShoppingListRepository,
) : ViewModel() {
    private val selectedTab = MutableStateFlow(ShoppingListTab.ToBuy)
    private val searchQuery = MutableStateFlow("")

    fun onTabSelected(tab: ShoppingListTab) {
        selectedTab.value = tab
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    val uiState: StateFlow<ShoppingListUiState> = combine(
        repository.observeItems(),
        selectedTab,
        searchQuery,
    ) { items, tab, query ->
        ShoppingListUiState(
            items = items.filter {
                it.isPurchased == (tab == ShoppingListTab.Purchased) && it.name.contains(
                    query.trim(),
                    ignoreCase = true
                )
            },
            selectedTab = tab,
            searchQuery = query,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ShoppingListUiState(),
    )

    fun increaseQuantity(item: ShoppingListItem) =
        updateItem(item.copy(quantityNeeded = item.quantityNeeded + 1))

    fun decreaseQuantity(item: ShoppingListItem) {
        item.quantityNeeded.takeIf { it > 1 }?.let {
            updateItem(item.copy(quantityNeeded = it - 1))
        }
    }

    fun addItem(name: String, quantityNeeded: Double, unit: String) {
        viewModelScope.launch {
            repository.addItem(
                name = name,
                quantityNeeded = quantityNeeded,
                unit = unit,
            )
        }
    }

    fun updateItem(item: ShoppingListItem) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun deleteItem(id: String) {
        viewModelScope.launch {
            repository.deleteItem(id)
        }
    }

    fun deletePurchasedItems() {
        viewModelScope.launch { repository.deletePurchasedItems() }
    }
}