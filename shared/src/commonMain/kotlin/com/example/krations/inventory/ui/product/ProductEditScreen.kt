package com.example.krations.inventory.ui.product

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProductEditScreen(
    productId: String?,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductEditViewModel = koinViewModel(),
) {
    LaunchedEffect(productId) {
        viewModel.load(productId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigateBackEvent.collectLatest { onDone() }
    }

    MaterialTheme {
        ProductEditContent(
            modifier = modifier,
            uiState = uiState,
            onBack = onDone,
            onNameChange = viewModel::onNameChange,
            onUnitChange = viewModel::onUnitChange,
            onCategoryChange = viewModel::onCategoryChange,
            onMinQuantityChange = viewModel::onMinQuantityChange,
            onExpiryAlertDaysChange = viewModel::onExpiryAlertDaysChange,
            onAddBatch = viewModel::addBatch,
            onBatchQuantityChange = viewModel::onBatchQuantityChange,
            onBatchExpirationChange = viewModel::onBatchExpirationChange,
            onSave = viewModel::save,
            onDelete = viewModel::delete,
        )
    }
}
