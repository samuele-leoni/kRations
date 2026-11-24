package com.example.krations.inventory.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krations.core.state.STOP_TIMEOUT
import com.example.krations.inventory.domain.CategoryRepository
import com.example.krations.inventory.domain.DeleteCategoryResult
import com.example.krations.inventory.domain.model.CategorySummary
import com.example.krations.inventory.ui.common.productCountLabel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryListViewModel(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val notice = MutableStateFlow<String?>(null)

    val uiState: StateFlow<CategoryListUiState> =
        combine(categoryRepository.observeCategoriesWithCount(), notice) { categories, message ->
            CategoryListUiState(categories = categories, notice = message)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
            initialValue = CategoryListUiState(),
        )

    fun onNoticeDismissed() {
        notice.value = null
    }

    fun delete(category: CategorySummary) {
        viewModelScope.launch {
            notice.value = when (val result = categoryRepository.deleteCategory(category.category.id)) {
                DeleteCategoryResult.Deleted -> null

                is DeleteCategoryResult.Blocked ->
                    category.nameOf(result)
            }
        }
    }
    private fun CategorySummary.nameOf(result: DeleteCategoryResult.Blocked): String =
        "${category.name} still has ${productCountLabel(result.productCount)} in it."
}
