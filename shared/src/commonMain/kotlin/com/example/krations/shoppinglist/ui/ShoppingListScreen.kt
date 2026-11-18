package com.example.krations.shoppinglist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import com.example.krations.shoppinglist.ui.components.ShoppingListItemDetail
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShoppingListScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingListViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showSheet by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ShoppingListItem?>(null) }
    ShoppingListContent(
        modifier = modifier,
        uiState = uiState,
        onTabSelected = viewModel::onTabSelected,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onIncrease = viewModel::increaseQuantity,
        onDecrease = viewModel::decreaseQuantity,
        onDelete = viewModel::deleteItem,
        onAddClick = { editingItem = null; showSheet = true },
        onItemClick = { editingItem = it; showSheet = true },
        onItemBought = viewModel::setItemPurchased,
        onDeleteAllPurchased = viewModel::deletePurchasedItems,
        onPurchaseAll = viewModel::setAllItemsPurchased,
    )
    if (showSheet) {
        ShoppingListItemDetail(
            initialItem = editingItem,
            onDismiss = { showSheet = false },
            onSave = { name, quantity, unit ->
                editingItem?.let {
                    viewModel.updateItem(it.copy(name = name, quantityNeeded = quantity, unit = unit))
                } ?: viewModel.addItem(name, quantity, unit)
                showSheet = false
            },
        )
    }
}