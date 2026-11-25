package com.example.krations.inventory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import com.example.krations.core.ui.utils.formatQuantity
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Add
import com.composables.icons.materialsymbols.rounded.Check
import com.composables.icons.materialsymbols.rounded.Close
import com.composables.icons.materialsymbols.rounded.Inventory_2
import com.composables.icons.materialsymbols.rounded.Search
import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.domain.model.StockStatus
import com.example.krations.inventory.domain.model.stockStatus
import com.example.krations.inventory.domain.model.Product
import com.example.krations.inventory.ui.common.inventoryIcon
import com.example.krations.inventory.ui.state.InventoryUiState

@Composable
fun InventoryContent(
    uiState: InventoryUiState,
    onSearchQueryChange: (String) -> Unit,
    onCategoryToggled: (String) -> Unit,
    onUncategorizedToggled: () -> Unit,
    onFiltersCleared: () -> Unit,
    onAddProduct: () -> Unit,
    onEditProduct: (String) -> Unit,
    onManageCategories: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().safeContentPadding().imePadding()) {
        Column(modifier = Modifier.fillMaxSize()) {
            SearchField(query = uiState.searchQuery, onQueryChange = onSearchQueryChange)

            CategoryChipsRow(
                categories = uiState.categories,
                selectedCategoryIds = uiState.selectedCategoryIds,
                uncategorizedSelected = uiState.uncategorizedSelected,
                onCategoryToggled = onCategoryToggled,
                onUncategorizedToggled = onUncategorizedToggled,
                onFiltersCleared = onFiltersCleared,
                onManageCategories = onManageCategories,
            )

            if (uiState.products.isEmpty()) {
                Text(
                    text = if (uiState.hasActiveFilters) {
                        "No products match your filters"
                    } else {
                        "Your inventory is empty"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp),
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(bottom = 88.dp),
                ) {
                    items(uiState.products, key = { product -> product.id }) { product ->
                        ProductCard(product = product, onClick = { onEditProduct(product.id) })
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
        trailingIcon = { Icon(MaterialSymbols.Rounded.Search, contentDescription = null) },
        shape = CircleShape,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        ),
    )
}

@Composable
private fun CategoryChipsRow(
    categories: List<Category>,
    selectedCategoryIds: Set<String>,
    uncategorizedSelected: Boolean,
    onCategoryToggled: (String) -> Unit,
    onUncategorizedToggled: () -> Unit,
    onFiltersCleared: () -> Unit,
    onManageCategories: () -> Unit,
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = selectedCategoryIds.isEmpty() && !uncategorizedSelected,
            onClick = onFiltersCleared,
            label = { Text("All") },
            shape = CircleShape,
        )
        categories.forEach { category ->
            FilterChip(
                selected = category.id in selectedCategoryIds,
                onClick = { onCategoryToggled(category.id) },
                leadingIcon = { Icon(inventoryIcon(category.iconName), contentDescription = null) },
                label = { Text(category.name) },
                shape = CircleShape,
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = Color.DarkGray,
                    iconColor = Color.DarkGray
                )
            )
        }

        FilterChip(
            selected = uncategorizedSelected,
            onClick = onUncategorizedToggled,
            leadingIcon = { Icon(MaterialSymbols.Rounded.Inventory_2, contentDescription = null) },
            label = { Text("No category") },
            shape = CircleShape,
            colors = FilterChipDefaults.filterChipColors(
                labelColor = Color.DarkGray,
                iconColor = Color.DarkGray
            ),
        )

        // Opens the category screens instead of filtering, so it never carries a selected state.
        FilterChip(
            selected = false,
            onClick = onManageCategories,
            leadingIcon = { Icon(MaterialSymbols.Rounded.Add, contentDescription = "Manage categories") },
            label = { Text("Category") },
            shape = CircleShape,
            colors = FilterChipDefaults.filterChipColors(
                labelColor = Color.DarkGray,
                iconColor = Color.DarkGray
            ),
        )
    }
}

@Composable
private fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector = inventoryIcon(product.iconName),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(32.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "(${product.stock.formatQuantity()} ${product.unit})",
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
        StockStatus.InStock -> colors.secondaryContainer
        StockStatus.LowStock -> colors.errorContainer
        StockStatus.OutOfStock -> colors.error
    }
    val iconTint = when (status) {
        StockStatus.InStock -> colors.onSecondaryContainer
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
                imageVector = if (status == StockStatus.InStock) MaterialSymbols.Rounded.Check else MaterialSymbols.Rounded.Close,
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
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Icon(MaterialSymbols.Rounded.Add, contentDescription = "Add product")
    }
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
            onCategoryToggled = {},
            onUncategorizedToggled = {},
            onFiltersCleared = {},
            onAddProduct = {},
            onEditProduct = {},
            onManageCategories = {},
        )
    }
}

private fun previewProducts(): List<Product> = listOf(
    previewProduct("1", "Milk", "Liters", stock = 5.0, iconName = "kitchen", minQuantity = 2.0, categoryId = "cat-2"),
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
    expiryAlertDays: Int = 7,
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
    expiryAlertDays = expiryAlertDays,
)

private fun previewCategory(id: String, name: String, iconName: String): Category =
    Category(
        id = id,
        name = name,
        iconName = iconName,
    )
