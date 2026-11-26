package com.example.krations.inventory.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Check
import com.composables.icons.materialsymbols.rounded.Search
import com.example.krations.core.navigation.ui.DetailScaffold
import com.example.krations.inventory.ui.common.EditField
import com.example.krations.inventory.ui.common.EditHeader
import com.example.krations.inventory.ui.common.EditablePillValue
import com.example.krations.inventory.ui.common.NoticeBanner
import com.example.krations.inventory.ui.common.Pill
import com.example.krations.inventory.ui.common.iconLabel
import com.example.krations.inventory.ui.common.inventoryIcon
import com.example.krations.inventory.ui.common.productCountLabel
import com.example.krations.inventory.ui.common.selectableIconNames

@Composable
@Preview(name = "Category edit", showBackground = true)
fun CategoryEditContent(
    modifier: Modifier = Modifier,
    uiState: CategoryEditUiState = CategoryEditUiState(name = "Cleaning", iconName = "household_supplies"),
    onBack: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onIconChange: (String) -> Unit = {},
    onBlockedMessageDismissed: () -> Unit = {},
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showIconPicker by remember { mutableStateOf(false) }

    DetailScaffold(
        modifier = modifier,
        header = {
            EditHeader(
                title = if (uiState.isCreate) "New category" else "Edit category",
                canDelete = !uiState.isCreate && !uiState.isLoading,
                onBack = onBack,
                onDelete = { showDeleteDialog = true },
                deleteContentDescription = "Delete category",
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {

            uiState.deleteBlockedCount?.let { count ->
                NoticeBanner(
                    message = "Move or delete its ${productCountLabel(count)} first.",
                    onDismiss = onBlockedMessageDismissed,
                )
            }

            EditField(label = "Name") {
                Pill(modifier = Modifier.fillMaxWidth()) {
                    EditablePillValue(
                        value = uiState.name,
                        onValueChange = onNameChange,
                        placeholder = "Category name",
                    )
                }
            }

            EditField(label = "Icon") {
                Pill(modifier = Modifier.fillMaxWidth().clickable(onClick = { showIconPicker = true })) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            imageVector = inventoryIcon(uiState.iconName),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = iconLabel(uiState.iconName),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                    Text("Cancel")
                }
                Button(
                    onClick = onSave,
                    enabled = uiState.canSave,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(if (uiState.isCreate) "Create" else "Save")
                }
            }
        }
    }

    if (showIconPicker) {
        IconPickerDialog(
            selected = uiState.iconName,
            onDismiss = { showIconPicker = false },
            onPick = { name ->
                onIconChange(name)
                showIconPicker = false
            },
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete this category?") },
            text = { Text("${uiState.name} will be removed. Products stay in your inventory.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    },
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun IconPickerDialog(
    selected: String,
    onDismiss: () -> Unit,
    onPick: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    val visible = remember(query) {
        selectableIconNames.filter { iconLabel(it).contains(query, ignoreCase = true) }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Icon") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search icons") },
                    singleLine = true,
                    trailingIcon = { Icon(MaterialSymbols.Rounded.Search, contentDescription = null) },
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    ),
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 56.dp),
                    modifier = Modifier.fillMaxWidth().height(320.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(visible, key = { it }) { name ->
                        val isSelected = name == selected
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(
                                    color = if (isSelected) {
                                        MaterialTheme.colorScheme.primaryContainer
                                    } else {
                                        Color.Transparent
                                    },
                                    shape = CircleShape,
                                )
                                .clickable { onPick(name) },
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = inventoryIcon(name),
                                contentDescription = iconLabel(name),
                                tint = if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = MaterialSymbols.Rounded.Check,
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.BottomEnd).size(14.dp),
                                )
                            }
                        }
                    }
                }

                if (visible.isEmpty()) {
                    Text(
                        text = "No icon matches \"$query\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        },
        confirmButton = {
            OutlinedButton(onClick = onDismiss) { Text("Close") }
        },
    )
}
