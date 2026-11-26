package com.example.krations.core.navigation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.example.krations.core.navigation.NavSection
import com.example.krations.profile.ui.ProfileIcon

@Composable
fun SectionScaffold(
    section: NavSection,
    onSelectSection: (NavSection) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Box(
                modifier = Modifier.windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top),
                ),
            ) {
                AppHeader(
                    title = section.headerTitle,
                    navigationIcon = {
                        IconButton(
                            onClick = onProfileClick,
                            modifier = Modifier.semantics {
                                contentDescription = "Profile and account access"
                            },
                        ) {
                            ProfileIcon(color = MaterialTheme.colorScheme.onSurface)
                        }
                    },
                    actions = { SyncAction() },
                )
            }
        },
        bottomBar = {
            AppFooter(
                selected = section,
                onSelect = onSelectSection,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            content = content,
        )
    }
}
