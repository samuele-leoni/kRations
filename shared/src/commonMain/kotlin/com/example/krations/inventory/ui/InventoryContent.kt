package com.example.krations.inventory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialicons.MaterialIcons
import com.composables.icons.materialicons.rounded.Add
import com.composables.icons.materialicons.rounded.Check
import com.composables.icons.materialicons.rounded.Cleaning_services
import com.composables.icons.materialicons.rounded.Close
import com.composables.icons.materialicons.rounded.Coffee_maker
import com.composables.icons.materialicons.rounded.Inventory_2
import com.composables.icons.materialicons.rounded.Kitchen
import com.composables.icons.materialicons.rounded.Local_grocery_store
import com.composables.icons.materialicons.rounded.Medication
import com.composables.icons.materialicons.rounded.Search
import com.composables.icons.materialicons.rounded.Warehouse
import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.domain.model.Product
import com.example.krations.inventory.ui.state.InventoryUiState

@Composable
fun InventoryContent(
    uiState: InventoryUiState,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onAddProduct: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().safeContentPadding().imePadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchField(query = uiState.searchQuery, onQueryChange = onSearchQueryChange)

            CategoryChipsRow(
                categories = uiState.categories,
                selectedCategoryId = uiState.selectedCategoryId,
                onCategorySelected = onCategorySelected,
            )

            if (uiState.products.isEmpty()) {
                Text(
                    text = "No products found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(uiState.products, key = { product -> product.id }) { product ->
                        ProductCard(product = product)
                    }
                }
            }
        }

        AddProductButton(
            onClick = onAddProduct,
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
        )
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search Inventory") },
        singleLine = true,
        trailingIcon = { Icon(MaterialIcons.Rounded.Search, contentDescription = null) },
        shape = RoundedCornerShape(percent = 50),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
    )
}

@Composable
private fun CategoryChipsRow(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategorySelected: (String?) -> Unit,
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = selectedCategoryId == null,
            onClick = { onCategorySelected(null) },
            label = { Text("All") },
        )
        categories.forEach { category ->
            FilterChip(
                selected = selectedCategoryId == category.id,
                onClick = { onCategorySelected(category.id) },
                leadingIcon = { Icon(icon(category.iconName), contentDescription = null) },
                label = { Text(category.name) },
            )
        }
    }
}

@Composable
private fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = icon(product.iconName),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "(${formatStock(product)})",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            StockStatusIndicator(status = product.stockStatus())
        }
    }
}

@Composable
private fun StockStatusIndicator(status: StockStatus) {
    val colors = MaterialTheme.colorScheme
    val containerColor = when (status) {
        StockStatus.InStock -> colors.primaryContainer
        StockStatus.LowStock -> colors.errorContainer
        StockStatus.OutOfStock -> colors.error
    }
    val iconTint = when (status) {
        StockStatus.InStock -> colors.onPrimaryContainer
        StockStatus.LowStock -> colors.onErrorContainer
        StockStatus.OutOfStock -> colors.onError
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(color = containerColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (status == StockStatus.InStock) MaterialIcons.Rounded.Check else MaterialIcons.Rounded.Close,
                contentDescription = status.label,
                tint = iconTint,
            )
        }
        Text(text = status.label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun AddProductButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Icon(MaterialIcons.Rounded.Add, contentDescription = "Add product")
    }
}

// The database stores the Material Symbols name shown in the mockups, and the icon library used by
// the project only covers the Material Icons set, so a few names are mapped to the closest icon.
private fun icon(iconName: String?): ImageVector = when (iconName) {
    "pill" -> MaterialIcons.Rounded.Medication
    "grocery" -> MaterialIcons.Rounded.Local_grocery_store
    "household_supplies" -> MaterialIcons.Rounded.Cleaning_services
    "shelves" -> MaterialIcons.Rounded.Warehouse
    "kitchen" -> MaterialIcons.Rounded.Kitchen
    "coffee_maker" -> MaterialIcons.Rounded.Coffee_maker
    else -> MaterialIcons.Rounded.Inventory_2
}

private enum class StockStatus(val label: String) {
    InStock("In Stock"),
    LowStock("Low Stock"),
    OutOfStock("Out of Stock"),
}

private fun Product.stockStatus(): StockStatus = when {
    stock <= 0.0 -> StockStatus.OutOfStock
    stock < minQuantity -> StockStatus.LowStock
    else -> StockStatus.InStock
}

@Preview(showBackground = true)
@Composable
private fun InventoryContentPreview() {
    PreviewInventory(products = previewProducts(), query = "")
}

@Preview(showBackground = true)
@Composable
private fun InventoryContentEmptyPreview() {
    PreviewInventory(products = emptyList(), query = "tomato")
}

@Composable
private fun PreviewInventory(products: List<Product>, query: String) {
    MaterialTheme {
        InventoryContent(
            uiState = InventoryUiState(
                products = products,
                categories = previewCategories(),
                searchQuery = query,
            ),
            onSearchQueryChange = {},
            onCategorySelected = {},
            onAddProduct = {},
        )
    }
}

private fun previewProducts(): List<Product> = listOf(
    previewProduct("1", "Milk", "Liters", stock = 5.0, iconName = "grocery", minQuantity = 2.0, categoryId = "cat-2"),
    previewProduct("2", "Aspirin", "pills", stock = 0.0, iconName = "pill", minQuantity = 10.0, categoryId = "cat-3"),
    previewProduct(
        "3",
        "Dish Soap",
        "Liters",
        stock = 1.0,
        iconName = "household_supplies",
        minQuantity = 2.0,
        categoryId = "cat-4",
    ),
)

private fun previewCategories(): List<Category> = listOf(
    previewCategory("cat-1", "Pantry", "shelves"),
    previewCategory("cat-2", "Fridge", "kitchen"),
    previewCategory("cat-3", "Medicines", "pill"),
    previewCategory("cat-4", "Cleaning", "household_supplies"),
)

private fun previewProduct(
    id: String,
    name: String,
    unit: String,
    stock: Double,
    iconName: String,
    minQuantity: Double = 0.0,
    categoryId: String? = null,
): Product = Product(
    id = id,
    categoryId = categoryId,
    name = name,
    description = null,
    iconName = iconName,
    unit = unit,
    stock = stock,
    minQuantity = minQuantity,
)

private fun previewCategory(id: String, name: String, iconName: String): Category =
    Category(
        id = id,
        name = name,
        iconName = iconName,
    )
