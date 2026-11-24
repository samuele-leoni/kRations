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

const val defaultIconName: String = "inventory_2"

// Categories store an icon by name, so this map is both the set a user can choose from and the set
// that can be rendered: a name that is not here falls back to defaultIconName.
private val inventoryIcons: Map<String, ImageVector> = mapOf(
    "inventory_2" to MaterialSymbols.Rounded.Inventory_2,
    "package" to MaterialSymbols.Rounded.Package,
    "grocery" to MaterialSymbols.Rounded.Grocery,
    "shelves" to MaterialSymbols.Rounded.Shelves,
    "kitchen" to MaterialSymbols.Rounded.Kitchen,
    "coffee_maker" to MaterialSymbols.Rounded.Coffee_maker,
    "water_drop" to MaterialSymbols.Rounded.Water_drop,
    "wine_bar" to MaterialSymbols.Rounded.Wine_bar,
    "rice_bowl" to MaterialSymbols.Rounded.Rice_bowl,
    "bakery_dining" to MaterialSymbols.Rounded.Bakery_dining,
    "egg" to MaterialSymbols.Rounded.Egg,
    "icecream" to MaterialSymbols.Rounded.Icecream,
    "fastfood" to MaterialSymbols.Rounded.Fastfood,
    "household_supplies" to MaterialSymbols.Rounded.Household_supplies,
    "cleaning_services" to MaterialSymbols.Rounded.Cleaning_services,
    "laundry" to MaterialSymbols.Rounded.Laundry,
    "soap" to MaterialSymbols.Rounded.Soap,
    "pill" to MaterialSymbols.Rounded.Pill,
    "medication" to MaterialSymbols.Rounded.Medication,
    "pet_supplies" to MaterialSymbols.Rounded.Pet_supplies,
)

val selectableIconNames: List<String> = inventoryIcons.keys.toList()

fun inventoryIcon(iconName: String?): ImageVector = inventoryIcons[iconName] ?: inventoryIcons.getValue(defaultIconName)

fun iconLabel(iconName: String): String =
    iconName.split('_').joinToString(separator = " ") { part -> part.replaceFirstChar(Char::uppercaseChar) }
