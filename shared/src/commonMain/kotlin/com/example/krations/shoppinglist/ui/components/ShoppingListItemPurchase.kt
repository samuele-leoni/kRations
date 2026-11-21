package com.example.krations.shoppinglist.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.rounded.Calendar_today
import com.example.krations.core.ui.utils.formatQuantity
import com.example.krations.shoppinglist.domain.model.ShoppingListItem
import kotlinx.datetime.LocalDate
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItemPurchase(
    item: ShoppingListItem,
    onDismiss: () -> Unit,
    onConfirm: (quantity: Double, expirationDate: String?) -> Unit,
) {
    var quantityText by rememberSaveable { mutableStateOf(item.quantityNeeded.formatQuantity()) }
    var expirationDate by rememberSaveable { mutableStateOf<String?>(null) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    val quantity = quantityText.toQuantityOrNull()

    FormBottomSheet(
        title = "Purchase ${item.name}",
        onDismiss = onDismiss,
    ) {
        QuantityField(
            value = quantityText,
            onValueChange = { quantityText = it },
        )
        OutlinedTextField(
            value = expirationDate ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Expiration Date (optional)") },
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        MaterialIcons.Rounded.Calendar_today,
                        contentDescription = "Choose expiration date"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = { quantity?.let { onConfirm(it, expirationDate) } },
            enabled = quantity != null,
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Confirm") }
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { expirationDate = it.formatDate() }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        expirationDate = null
                        showDatePicker = false
                    }) { Text("Clear") }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

private fun Long.formatDate(): String =
    LocalDate.fromEpochDays(milliseconds.inWholeDays.toInt()).toString()