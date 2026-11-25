package com.example.krations.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.krations.core.navigation.AppNavigation
import com.example.krations.core.navigation.AppRoute
import com.example.krations.profile.ui.ProfileIcon
import com.example.krations.shoppinglist.ui.ShoppingListScreen

private val AppBackground = Color(0xFFFFF8FF)
private val Ink = Color(0xFF211F26)

private enum class HomeTab {
    Inventory,
    ShoppingList,
}

@Composable
fun HomeScreen(email: String?, onProfileClick: () -> Unit) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.Inventory) }
    val inventoryBackStack = remember { mutableStateListOf<AppRoute>(AppRoute.Inventory) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppBackground,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Inventory,
                    onClick = { selectedTab = HomeTab.Inventory },
                    icon = {},
                    label = { Text("Inventory") },
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.ShoppingList,
                    onClick = { selectedTab = HomeTab.ShoppingList },
                    icon = {},
                    label = { Text("Shopping list") },
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        ) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text("kRations", fontSize = 22.sp, fontWeight = FontWeight.Medium, color = Ink)
                    if (email != null) {
                        Text(email, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                TextButton(
                    onClick = onProfileClick,
                    modifier = Modifier.semantics {
                        contentDescription = "Profile and account access"
                    },
                ) {
                    ProfileIcon()
                }
            }

            when (selectedTab) {
                HomeTab.Inventory -> AppNavigation(
                    backStack = inventoryBackStack,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                )
                HomeTab.ShoppingList -> ShoppingListScreen(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                )
            }
        }
    }
}
