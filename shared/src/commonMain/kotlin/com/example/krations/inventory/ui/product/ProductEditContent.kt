package com.example.krations.inventory.ui.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Add
import com.composables.icons.materialsymbols.rounded.Arrow_back
import com.composables.icons.materialsymbols.rounded.Arrow_drop_down
import com.composables.icons.materialsymbols.rounded.Delete
import com.composables.icons.materialsymbols.rounded.Remove
import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.ui.common.EditField
import com.example.krations.inventory.ui.common.EditHeader
import com.example.krations.inventory.ui.common.EditablePillValue
import com.example.krations.inventory.ui.common.Pill
import com.example.krations.inventory.ui.common.SelectPill
import kotlinx.datetime.LocalDate

@Composable
@Preview(name = "Product edit", showBackground = true)
fun ProductEditContent(
    modifier: Modifier = Modifier,
    uiState: ProductEditUiState = ProductEditUiState(
        name = "Dish Soap",
        categoryId = "cat-4",
        unit = "Liters",
        minQuantity = 5.0,
        categories = listOf(Category("cat-4", "Cleaning", "household_supplies")),
        batches = listOf(BatchDraft(expirationDate = "2026-12-15"), BatchDraft(quantity = 3)),
    ),
    onBack: () -> Unit = {},
    onNameChange: (String) -> Unit = {},
    onUnitChange: (String) -> Unit = {},
    onCategoryChange: (String?) -> Unit = {},
    onCreateCategory: () -> Unit = {},
    onMinQuantityChange: (Double) -> Unit = {},
    onExpiryAlertDaysChange: (Int) -> Unit = {},
    onAddBatch: () -> Unit = {},
    onBatchQuantityChange: (Int, Int) -> Unit = { _, _ -> },
    onBatchExpirationChange: (Int, String?) -> Unit = { _, _ -> },
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var openedPicker by remember { mutableStateOf<String?>(null) }
    var batchBeingDated by remember { mutableStateOf<Int?>(null) }

    Box(modifier = modifier.fillMaxSize().safeContentPadding().imePadding()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
        ) {
            EditHeader(
                title = if (uiState.isCreate) "Create product" else "Edit product",
                canDelete = !uiState.isCreate && !uiState.isLoading,
                onBack = onBack,
                onDelete = { showDeleteDialog = true },
            )

            EditField(label = "Name") {
                Pill(modifier = Modifier.fillMaxWidth()) {
                    EditablePillValue(
                        value = uiState.name,
                        onValueChange = onNameChange,
                        placeholder = "Product name",
                    )
                }
            }

            EditField(label = "Category") {
                SelectPill(
                    text = uiState.categories.find { it.id == uiState.categoryId }?.name ?: "No category",
                    onClick = { openedPicker = CATEGORY_PICKER },
                )
            }

            EditField(label = "Unit") {
                SelectPill(text = uiState.unit, onClick = { openedPicker = UNIT_PICKER })
            }

            MinimumStockField(value = uiState.minQuantity, onValueChange = onMinQuantityChange)

            EditField(label = "Expiry Alert Days") {
                StepperPill(value = uiState.expiryAlertDays, onValueChange = onExpiryAlertDaysChange)
            }

            BatchesSection(
                batches = uiState.batches,
                onAdd = onAddBatch,
                onQuantityChange = onBatchQuantityChange,
                onExpirationClick = { index -> batchBeingDated = index },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
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

    openedPicker?.let { which ->
        val options: List<Pair<String, String?>> = when (which) {
            CATEGORY_PICKER -> buildList {
                add("No category" to null)
                uiState.categories.forEach { add(it.name to it.id) }
                add("New category" to NEW_CATEGORY)
            }

            else -> productUnits.map { it to it }
        }

        OptionPickerSheet(
            title = if (which == CATEGORY_PICKER) "Category" else "Unit",
            options = options,
            onDismiss = { openedPicker = null },
            onSelect = { value ->
                when {
                    value == NEW_CATEGORY -> onCreateCategory()

                    which == CATEGORY_PICKER -> onCategoryChange(value)

                    else -> value?.let(onUnitChange)
                }
                openedPicker = null
            },
        )
    }

    batchBeingDated?.let { index ->
        BatchExpirationDialog(
            onDismiss = { batchBeingDated = null },
            onPick = { date ->
                onBatchExpirationChange(index, date)
                batchBeingDated = null
            },
            onClear = {
                onBatchExpirationChange(index, null)
                batchBeingDated = null
            },
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete this product?") },
            text = { Text("${uiState.name} will be removed from your inventory.") },
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
private fun MinimumStockField(
    value: Double,
    onValueChange: (Double) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(20.dp),
            )
            .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 2.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Minimum Stock",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            Pill(modifier = Modifier.width(56.dp)) {
                Text(text = value.toInt().toString(), style = MaterialTheme.typography.bodyLarge)
            }
        }
        Slider(
            value = value.toFloat().coerceIn(0f, MAX_MIN_QUANTITY),
            onValueChange = { onValueChange(it.toDouble()) },
            valueRange = 0f..MAX_MIN_QUANTITY,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            ),
        )
    }
}

@Composable
private fun BatchesSection(
    batches: List<BatchDraft>,
    onAdd: () -> Unit,
    onQuantityChange: (Int, Int) -> Unit,
    onExpirationClick: (Int) -> Unit,
) {
    Text(
        text = "Batches",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .paddingFromBaseline(top = 28.dp, bottom = 4.dp)
            .padding(horizontal = 16.dp),
    )

    batches.forEachIndexed { index, batch ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(20.dp),
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = batch.expirationDate.formatExpirationLabel(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onExpirationClick(index) },
            )
            StepperPill(
                value = batch.quantity,
                onValueChange = { onQuantityChange(index, it - batch.quantity) },
            )
        }
    }

    Button(
        onClick = onAdd,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
    ) {
        Text("Add New Batch")
    }
}

@Composable
private fun StepperPill(
    value: Int,
    onValueChange: (Int) -> Unit,
) {
    Pill(contentPadding = PaddingValues(4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            StepperButton(
                icon = MaterialSymbols.Rounded.Remove,
                contentDescription = "One less",
                onClick = { onValueChange(value - 1) },
            )
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center,
            )
            StepperButton(
                icon = MaterialSymbols.Rounded.Add,
                contentDescription = "One more",
                onClick = { onValueChange(value + 1) },
            )
        }
    }
}

@Composable
private fun StepperButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(18.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OptionPickerSheet(
    title: String,
    options: List<Pair<String, String?>>,
    onDismiss: () -> Unit,
    onSelect: (String?) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberBottomSheetState(
            initialValue = SheetValue.Hidden,
            enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded),
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            options.forEach { (text, value) ->
                TextButton(
                    onClick = { onSelect(value) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BatchExpirationDialog(
    onDismiss: () -> Unit,
    onPick: (String) -> Unit,
    onClear: () -> Unit,
) {
    val state = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    state.selectedDateMillis?.let { millis ->
                        onPick(millis.toIsoDate())
                    } ?: onDismiss()
                },
            ) { Text("OK") }
        },
        dismissButton = { TextButton(onClick = onClear) { Text("No expiry") } },
    ) {
        DatePicker(state = state)
    }
}

private fun Long.toIsoDate(): String =
    LocalDate.fromEpochDays((this / MILLIS_PER_DAY).toInt()).toString()

/** "2026-12-15" in, "Exp. Dec 15, 2026" out, the way the mockup labels a batch. */
private fun String?.formatExpirationLabel(): String {
    val date = this ?: return "Add expiration date"
    val parts = date.split("-")
    if (parts.size != 3) return "Exp. $date"
    val month = parts[1].toIntOrNull()?.let { MONTH_NAMES.getOrNull(it - 1) } ?: return "Exp. $date"
    return "Exp. $month ${parts[2].toIntOrNull() ?: "??"}, ${parts[0]}"
}

private val MONTH_NAMES = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",
)

private const val MAX_MIN_QUANTITY = 50f
private const val MILLIS_PER_DAY = 86_400_000L
private const val CATEGORY_PICKER = "category"
private const val UNIT_PICKER = "unit"

// Not a category id: it is the row that leaves the picker and opens the editor instead.
private const val NEW_CATEGORY = "new-category"
