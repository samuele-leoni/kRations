package com.example.krations.inventory.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun InventoryScreen(
    onAddProduct: () -> Unit,
    onEditProduct: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InventoryViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MaterialTheme {
        InventoryContent(
            modifier = modifier,
            uiState = uiState,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            onCategorySelected = viewModel::onCategorySelected,
            onNoCategorySelected = viewModel::onNoCategorySelected,
            onAddProduct = onAddProduct,
            onEditProduct = onEditProduct,
        )
    }
}
