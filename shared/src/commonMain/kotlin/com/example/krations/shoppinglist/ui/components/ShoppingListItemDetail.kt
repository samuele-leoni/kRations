package com.example.krations.shoppinglist.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.rounded.Save
import com.example.krations.core.ui.utils.formatQuantity
import com.example.krations.shoppinglist.domain.model.ShoppingListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListItemDetail(
    initialItem: ShoppingListItem?,
    onDismiss: () -> Unit,
    onSave: (name: String, quantity: Double, unit: String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf(initialItem?.name ?: "") }
    var quantityText by rememberSaveable {
        mutableStateOf(
            initialItem?.quantityNeeded?.formatQuantity() ?: "1"
        )
    }
    var unit by rememberSaveable { mutableStateOf(initialItem?.unit ?: "") }

    val quantity = quantityText.replace(',', '.').toDoubleOrNull()
    val isValid = name.isNotBlank() && quantity != null && quantity > 0

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
            Text(
                if (initialItem == null) "Add item" else "Edit item",
                style = MaterialTheme.typography.titleLarge
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = quantityText,
                onValueChange = { quantityText = it },
                isError = quantity == null || quantity <= 0,
                label = { Text("Quantity") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            )
            OutlinedTextField(
                value = unit,
                onValueChange = { unit = it },
                label = { Text("Unit") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { quantity?.let { onSave(name.trim(), it, unit.trim()) } },
                enabled = isValid,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Icon(MaterialIcons.Rounded.Save, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Save")
            }
        }
    }
}