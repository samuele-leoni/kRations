package com.example.krations.inventory.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class InventoryViewModel(
    productRepository: ProductRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedCategoryId = MutableStateFlow<String?>(null)

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
    }

    fun onCategorySelected(categoryId: String?) {
        selectedCategoryId.value = categoryId
    }

    // The search field fires on every keystroke, so the filter is debounced before reaching SQLite.
    private val products = combine(searchQuery, selectedCategoryId) { query, categoryId ->
        Filters(query.trim(), categoryId)
    }
        .debounce(TEXT_DEBOUNCE)
        .flatMapLatest { (query, categoryId) -> productRepository.observeProducts(query, categoryId) }

    val uiState: StateFlow<InventoryUiState> = combine(
        products,
        categoryRepository.observeCategories(),
        searchQuery,
        selectedCategoryId,
    ) { items, categories, query, categoryId ->
        InventoryUiState(
            products = items,
            categories = categories,
            searchQuery = query,
            selectedCategoryId = categoryId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
        initialValue = InventoryUiState(),
    )

    private data class Filters(val query: String, val categoryId: String?)

    companion object {
        private val TEXT_DEBOUNCE = 300.milliseconds
        private const val STOP_TIMEOUT = 5_000L
    }
}
