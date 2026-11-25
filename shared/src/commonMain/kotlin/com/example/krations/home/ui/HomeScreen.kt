package com.example.krations.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.krations.core.navigation.AppNavigation
import com.example.krations.core.navigation.AppRoute
import com.example.krations.core.navigation.NavSection
import com.example.krations.core.navigation.ui.AppFooter
import com.example.krations.core.navigation.ui.AppHeader
import com.example.krations.core.navigation.ui.SyncAction
import com.example.krations.profile.ui.ProfileIcon
import com.example.krations.shoppinglist.ui.ShoppingListScreen

private val AppBackground = Color(0xFFFFF8FF)

@Composable
fun HomeScreen(onProfileClick: () -> Unit) {
    var selectedSection by rememberSaveable { mutableStateOf(NavSection.Inventory) }
    val inventoryBackStack = remember { mutableStateListOf<AppRoute>(AppRoute.Inventory) }

    // Detail screens such as create or edit product draw their own header and cover the whole
    // screen, so the shell header and footer are only shown while a section sits at its root.
    val showSectionChrome = inventoryBackStack.size == 1

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = AppBackground,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            if (showSectionChrome) {
                AppFooter(
                    selected = selectedSection,
                    onSelect = { selectedSection = it },
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
            if (showSectionChrome) {
                AppHeader(
                    title = when (selectedSection) {
                        NavSection.Home -> "kRations"
                        else -> selectedSection.label
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    navigationIcon = {
                        IconButton(
                            onClick = onProfileClick,
                            modifier = Modifier.semantics {
                                contentDescription = "Profile and account access"
                            },
                        ) {
                            ProfileIcon()
                        }
                    },
                    actions = { SyncAction() },
                )
            }

            when (selectedSection) {
                NavSection.Home -> {
                }

                NavSection.Inventory -> AppNavigation(
                    backStack = inventoryBackStack,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                )

                NavSection.ShoppingList -> ShoppingListScreen(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                )
            }
        }
    }
}
