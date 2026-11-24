package com.example.krations.inventory.ui.common

import androidx.compose.ui.graphics.vector.ImageVector
import com.composables.icons.materialsymbols.MaterialSymbols
import com.composables.icons.materialsymbols.rounded.Bakery_dining
import com.composables.icons.materialsymbols.rounded.Cleaning_services
import com.composables.icons.materialsymbols.rounded.Coffee_maker
import com.composables.icons.materialsymbols.rounded.Egg
import com.composables.icons.materialsymbols.rounded.Fastfood
import com.composables.icons.materialsymbols.rounded.Grocery
import com.composables.icons.materialsymbols.rounded.Household_supplies
import com.composables.icons.materialsymbols.rounded.Icecream
import com.composables.icons.materialsymbols.rounded.Inventory_2
import com.composables.icons.materialsymbols.rounded.Kitchen
import com.composables.icons.materialsymbols.rounded.Laundry
import com.composables.icons.materialsymbols.rounded.Medication
import com.composables.icons.materialsymbols.rounded.Package
import com.composables.icons.materialsymbols.rounded.Pet_supplies
import com.composables.icons.materialsymbols.rounded.Pill
import com.composables.icons.materialsymbols.rounded.Rice_bowl
import com.composables.icons.materialsymbols.rounded.Shelves
import com.composables.icons.materialsymbols.rounded.Soap
import com.composables.icons.materialsymbols.rounded.Water_drop
import com.composables.icons.materialsymbols.rounded.Wine_bar

val defaultIconName: String = "inventory_2"

val selectableIconNames: List<String> = listOf(
    "inventory_2",
    "package",
    "grocery",
    "shelves",
    "kitchen",
    "coffee_maker",
    "water_drop",
    "wine_bar",
    "rice_bowl",
    "bakery_dining",
    "egg",
    "icecream",
    "fastfood",
    "household_supplies",
    "cleaning_services",
    "laundry",
    "soap",
    "pill",
    "medication",
    "pet_supplies",
)

fun inventoryIcon(iconName: String?): ImageVector = when (iconName) {
    "package" -> MaterialSymbols.Rounded.Package
    "grocery" -> MaterialSymbols.Rounded.Grocery
    "household_supplies" -> MaterialSymbols.Rounded.Household_supplies
    "shelves" -> MaterialSymbols.Rounded.Shelves
    "kitchen" -> MaterialSymbols.Rounded.Kitchen
    "coffee_maker" -> MaterialSymbols.Rounded.Coffee_maker
    "water_drop" -> MaterialSymbols.Rounded.Water_drop
    "wine_bar" -> MaterialSymbols.Rounded.Wine_bar
    "rice_bowl" -> MaterialSymbols.Rounded.Rice_bowl
    "bakery_dining" -> MaterialSymbols.Rounded.Bakery_dining
    "egg" -> MaterialSymbols.Rounded.Egg
    "icecream" -> MaterialSymbols.Rounded.Icecream
    "fastfood" -> MaterialSymbols.Rounded.Fastfood
    "cleaning_services" -> MaterialSymbols.Rounded.Cleaning_services
    "laundry" -> MaterialSymbols.Rounded.Laundry
    "soap" -> MaterialSymbols.Rounded.Soap
    "pill" -> MaterialSymbols.Rounded.Pill
    "medication" -> MaterialSymbols.Rounded.Medication
    "pet_supplies" -> MaterialSymbols.Rounded.Pet_supplies
    else -> MaterialSymbols.Rounded.Inventory_2
}

fun iconLabel(iconName: String): String =
    iconName.split('_').joinToString(separator = " ") { part -> part.replaceFirstChar(Char::uppercaseChar) }
