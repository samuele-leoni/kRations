package com.example.krations.inventory.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Add
import com.composables.icons.materialsymbols.rounded.Close
import com.composables.icons.materialsymbols.rounded.Delete
import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.domain.model.CategorySummary
import com.example.krations.inventory.ui.common.EditHeader
import com.example.krations.inventory.ui.common.inventoryIcon

@Composable
@Preview(name = "Category list", showBackground = true)
fun CategoryListContent(
    modifier: Modifier = Modifier,
    uiState: CategoryListUiState = CategoryListUiState(
        categories = listOf(
            CategorySummary(Category("cat-1", "Pantry", "shelves"), 4),
            CategorySummary(Category("cat-2", "Cleaning", "household_supplies"), 0),
        ),
        notice = "Pantry still has 4 products in it.",
    ),
    onBack: () -> Unit = {},
    onNewCategory: () -> Unit = {},
    onEditCategory: (String) -> Unit = {},
    onDelete: (CategorySummary) -> Unit = {},
    onNoticeDismissed: () -> Unit = {},
) {
    var categoryToDelete by remember { mutableStateOf<CategorySummary?>(null) }

    Box(modifier = modifier.fillMaxSize().safeContentPadding().imePadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            EditHeader(title = "Categories", canDelete = false, onBack = onBack, onDelete = {})

            uiState.notice?.let { message ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(16.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onNoticeDismissed) {
                        Icon(MaterialSymbols.Rounded.Close, contentDescription = "Dismiss")
                    }
                }
            }

            if (uiState.categories.isEmpty()) {
                Text(
                    text = "No categories yet. Create one to keep related products together.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 88.dp),
                ) {
                    items(uiState.categories, key = { summary -> summary.category.id }) { summary ->
                        CategoryCard(
                            summary = summary,
                            onClick = { onEditCategory(summary.category.id) },
                            onDelete = { categoryToDelete = summary },
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onNewCategory,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
        ) {
            Icon(MaterialSymbols.Rounded.Add, contentDescription = "New category")
        }
    }

    categoryToDelete?.let { summary ->
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            title = { Text("Delete this category?") },
            text = { Text("${summary.category.name} will be removed. Products stay in your inventory.") },
            confirmButton = {
                Button(
                    onClick = {
                        categoryToDelete = null
                        onDelete(summary)
                    },
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { categoryToDelete = null }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun CategoryCard(
    summary: CategorySummary,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val blocked = summary.productCount > 0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = inventoryIcon(summary.category.iconName),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = summary.category.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "(${summary.productCount} ${if (summary.productCount == 1) "product" else "products"})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = MaterialSymbols.Rounded.Delete,
                    contentDescription = if (blocked) "Delete blocked" else "Delete category",
                    tint = if (blocked) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                )
            }
        }
    }
}
