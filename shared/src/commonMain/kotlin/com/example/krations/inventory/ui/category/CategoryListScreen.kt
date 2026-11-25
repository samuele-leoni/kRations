package com.example.krations.inventory.ui.category

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoryListScreen(
    onBack: () -> Unit,
    onNewCategory: () -> Unit,
    onEditCategory: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryListViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MaterialTheme {
        CategoryListContent(
            modifier = modifier,
            uiState = uiState,
            onBack = onBack,
            onNewCategory = onNewCategory,
            onEditCategory = onEditCategory,
            onDelete = viewModel::delete,
            onNoticeDismissed = viewModel::onNoticeDismissed,
        )
    }
}
