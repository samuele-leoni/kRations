package com.example.krations.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList

class AppNavigationState(
    sectionRoots: Map<NavSection, AppRoute>,
    selectedSectionState: MutableState<NavSection>,
) {
    private val stacks: Map<NavSection, SnapshotStateList<AppRoute>> =
        sectionRoots.mapValues { (_, root) -> mutableStateListOf(root) }

    private var selectedSection by selectedSectionState

    val backStack: SnapshotStateList<AppRoute>
        get() = stacks.getValue(selectedSection)

    fun select(section: NavSection) {
        selectedSection = section
    }

    fun push(route: AppRoute) {
        backStack.add(route)
    }

    fun pop() {
        backStack.removeLastOrNull()
    }
}

@Composable
fun rememberAppNavigationState(): AppNavigationState {
    val selectedSection = rememberSaveable { mutableStateOf(NavSection.Inventory) }

    return remember {
        AppNavigationState(
            sectionRoots = NavSection.entries.associateWith { section -> section.rootRoute },
            selectedSectionState = selectedSection,
        )
    }
}
