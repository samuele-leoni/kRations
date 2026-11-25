package com.example.krations.inventory.ui.category

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CategoryEditScreen(
    categoryId: String?,
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryEditViewModel = koinViewModel(),
) {
    LaunchedEffect(categoryId) {
        viewModel.load(categoryId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.navigateBackEvent.collectLatest { onDone() }
    }

    MaterialTheme {
        CategoryEditContent(
            modifier = modifier,
            uiState = uiState,
            onBack = onDone,
            onNameChange = viewModel::onNameChange,
            onIconChange = viewModel::onIconChange,
            onBlockedMessageDismissed = viewModel::onBlockedMessageDismissed,
            onSave = viewModel::save,
            onDelete = viewModel::delete,
        )
    }
}
