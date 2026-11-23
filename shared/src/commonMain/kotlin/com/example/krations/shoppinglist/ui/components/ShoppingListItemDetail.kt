package com.example.krations.shoppinglist.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Save
import com.example.krations.core.ui.utils.formatQuantity
import com.example.krations.shoppinglist.domain.model.ShoppingListItem

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

    val quantity = quantityText.toQuantityOrNull()
    val isValid = name.isNotBlank() && quantity != null

    FormBottomSheet(
        title = if (initialItem == null) "Add item" else "Edit item",
        onDismiss = onDismiss,
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        QuantityField(value = quantityText, onValueChange = { quantityText = it })
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
            Icon(MaterialSymbols.Rounded.Save, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Save")
        }
    }
}