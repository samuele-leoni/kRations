package com.example.krations.core.navigation.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.krations.core.navigation.NavSection

@Composable
fun AppFooter(
    selected: NavSection,
    onSelect: (NavSection) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        NavSection.entries.forEach { section ->
            NavigationBarItem(
                selected = section == selected,
                onClick = { onSelect(section) },
                icon = { Icon(section.icon, contentDescription = section.label) },
                label = { Text(section.label) },
                alwaysShowLabel = true,
            )
        }
    }
}
