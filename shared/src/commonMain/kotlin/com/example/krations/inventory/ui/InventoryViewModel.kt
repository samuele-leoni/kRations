package com.example.krations.inventory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krations.core.state.STOP_TIMEOUT
import com.example.krations.inventory.domain.CategoryRepository
import com.example.krations.inventory.domain.ProductRepository
import com.example.krations.inventory.ui.state.InventoryUiState
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class InventoryViewModel(
    productRepository: ProductRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedCategoryIds = MutableStateFlow<Set<String>>(emptySet())
    private val uncategorizedSelected = MutableStateFlow(false)

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onCategoryToggled(categoryId: String) {
        selectedCategoryIds.update { current ->
            if (categoryId in current) current - categoryId else current + categoryId
        }
    }

    fun onUncategorizedToggled() {
        uncategorizedSelected.update { current -> !current }
    }

    fun onFiltersCleared() {
        selectedCategoryIds.value = emptySet()
        uncategorizedSelected.value = false
    }

    private val products =
        combine(searchQuery, selectedCategoryIds, uncategorizedSelected) { query, categoryIds, uncategorized ->
            Filters(query.trim(), categoryIds, uncategorized)
        }
            .debounce(TEXT_DEBOUNCE)
            .flatMapLatest { (query, categoryIds, uncategorized) ->
                productRepository.observeProducts(query, categoryIds, uncategorized)
            }

    val uiState: StateFlow<InventoryUiState> = combine(
        products,
        categoryRepository.observeCategories(),
        searchQuery,
        selectedCategoryIds,
        uncategorizedSelected,
    ) { items, categories, query, categoryIds, uncategorized ->
        InventoryUiState(
            products = items,
            categories = categories,
            searchQuery = query,
            selectedCategoryIds = categoryIds,
            uncategorizedSelected = uncategorized,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
        initialValue = InventoryUiState(),
    )

    private data class Filters(
        val query: String,
        val categoryIds: Set<String>,
        val includeUncategorized: Boolean,
    )

    companion object {
        private val TEXT_DEBOUNCE = 300.milliseconds
    }
}
