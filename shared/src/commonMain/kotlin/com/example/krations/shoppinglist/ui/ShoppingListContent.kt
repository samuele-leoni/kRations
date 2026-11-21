package com.example.krations.shoppinglist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.rounded.Add
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import com.example.krations.shoppinglist.ui.components.ShoppingListContentItem
import com.example.krations.shoppinglist.ui.state.ShoppingListTab
import com.example.krations.shoppinglist.ui.state.ShoppingListUiState

@Composable
fun ShoppingListContent(
    uiState: ShoppingListUiState,
    onTabSelected: (ShoppingListTab) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onIncrease: (ShoppingListItem) -> Unit,
    onDecrease: (ShoppingListItem) -> Unit,
    onDelete: (String) -> Unit,
    onItemBought: (ShoppingListItem) -> Unit,
    onItemClick: (ShoppingListItem) -> Unit,
    onAddClick: () -> Unit,
    onDeleteAllPurchased: () -> Unit,
    onPurchaseAll: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val emptyMessage = when {
        uiState.searchQuery.isNotBlank() -> "No items found"
        uiState.selectedTab == ShoppingListTab.Purchased -> "No purchased items"
        else -> "Add some items to your shopping list"
    }
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row (
                horizontalArrangement = Arrangement.spacedBy(8.dp),

            ) {
                FilterChip(
                    selected = uiState.selectedTab == ShoppingListTab.ToBuy,
                    onClick = { onTabSelected(ShoppingListTab.ToBuy) },
                    label = { Text("To Buy") },
                )
                FilterChip(
                    selected = uiState.selectedTab == ShoppingListTab.Purchased,
                    onClick = { onTabSelected(ShoppingListTab.Purchased) },
                    label = { Text("Purchased") },
                )
            }
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Search") },
            )
            uiState.items.takeIf { it.isNotEmpty() }?.let { list ->
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp),
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(list, key = {
                        it.id
                    }) {
                        ShoppingListContentItem(
                            it,
                            onIncrease = { onIncrease(it) },
                            onDecrease = { onDecrease(it) },
                            onDelete = { onDelete(it.id) },
                            onItemBought = { onItemBought(it) },
                            onClick = { onItemClick(it) },
                        )
                    }
                }
            } ?: Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = emptyMessage,
                    textAlign = TextAlign.Center,
                )
            }
            Row (modifier = Modifier.fillMaxWidth()) {
                if (uiState.selectedTab == ShoppingListTab.Purchased && uiState.items.isNotEmpty()) {
                    Button(modifier = Modifier.weight(1f), onClick = onDeleteAllPurchased) {
                        Text("Delete All")
                    }
                } else if (uiState.selectedTab == ShoppingListTab.ToBuy) {
                    FilledTonalIconButton(
                        onClick = onAddClick,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    ) {
                        Icon(
                            MaterialIcons.Rounded.Add,
                            contentDescription = "Add new item to the list",
                        )
                    }

                    if(uiState.items.isNotEmpty()) {
                        Button(modifier = Modifier.weight(1f).padding(start = 8.dp), onClick = onPurchaseAll) {
                            Text("Purchase All")
                        }
                    }
                }
            }
        }
    }
}