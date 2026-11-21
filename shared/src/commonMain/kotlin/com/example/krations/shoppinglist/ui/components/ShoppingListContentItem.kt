package com.example.krations.shoppinglist.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.rounded.Add
import com.composables.icons.materialicons.rounded.Delete
import com.composables.icons.materialicons.rounded.Remove
import com.composables.icons.materialicons.rounded.Remove_shopping_cart
import com.composables.icons.materialicons.rounded.Shopping_cart_checkout
import com.example.krations.core.ui.utils.formatQuantity
import com.example.krations.shoppinglist.domain.model.ShoppingListItem

@Composable
fun ShoppingListContentItem(
    item: ShoppingListItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onDelete: () -> Unit,
    onItemBought: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Column (modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                    if (item.unit.isNotBlank()) {
                        Text(
                            " (${item.unit})",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onDecrease
                    ) {
                        Icon(
                            imageVector = MaterialIcons.Rounded.Remove,
                            contentDescription = "Decrease quantity"
                        )
                    }
                    Text(
                        text = item.quantityNeeded.formatQuantity(),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    IconButton(
                        onClick = onIncrease
                    ) {
                        Icon(
                            imageVector = MaterialIcons.Rounded.Add,
                            contentDescription = "Increase quantity"
                        )
                    }
                }
            }
            if (item.isPurchased) {
                FilledTonalIconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = MaterialIcons.Rounded.Delete,
                        contentDescription = "Remove from shopping list"
                    )
                }
            } else {
                FilledTonalIconButton(
                    onClick = onDelete
                ) {
                    Icon(
                        imageVector = MaterialIcons.Rounded.Remove_shopping_cart,
                        contentDescription = "Remove from shopping list"
                    )
                }
                FilledTonalIconButton(
                    onClick = onItemBought
                ) {
                    Icon(
                        imageVector = MaterialIcons.Rounded.Shopping_cart_checkout,
                        contentDescription = "Set as bought"
                    )
                }
            }
        }
    }
}