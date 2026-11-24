package com.example.krations.inventory.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krations.inventory.domain.CategoryRepository
import com.example.krations.inventory.domain.DeleteCategoryResult
import com.example.krations.inventory.domain.model.Category
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryEditViewModel(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    private val draft = MutableStateFlow(CategoryEditUiState())

    private var loadedCategory: Category? = null

    private val backEvents = Channel<Unit>(Channel.CONFLATED)

    val navigateBackEvent = backEvents.receiveAsFlow()

    val uiState: StateFlow<CategoryEditUiState> = draft.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
        initialValue = CategoryEditUiState(),
    )

    fun load(categoryId: String?) {
        while (backEvents.tryReceive().isSuccess) {
            // A back event queued but never consumed must not pop the screen we are opening now.
        }
        loadedCategory = null
        draft.value = CategoryEditUiState(isCreate = categoryId == null, isLoading = true)

        viewModelScope.launch {
            val category = categoryId?.let { categoryRepository.getCategory(it) }
            loadedCategory = category

            draft.value = if (category == null) {
                CategoryEditUiState(isCreate = true)
            } else {
                CategoryEditUiState(
                    isCreate = false,
                    name = category.name,
                    iconName = category.iconName,
                )
            }
        }
    }

    fun onNameChange(value: String) {
        draft.value = draft.value.copy(name = value, deleteBlockedCount = null)
    }

    fun onIconChange(value: String) {
        draft.value = draft.value.copy(iconName = value)
    }

    fun onBlockedMessageDismissed() {
        draft.value = draft.value.copy(deleteBlockedCount = null)
    }

    fun save() {
        val state = draft.value
        if (!state.canSave) return
        val name = state.name.trim()

        viewModelScope.launch {
            val existing = loadedCategory
            if (existing == null) {
                categoryRepository.addCategory(name = name, iconName = state.iconName)
            } else {
                categoryRepository.updateCategory(existing.copy(name = name, iconName = state.iconName))
            }

            backEvents.trySend(Unit)
        }
    }

    fun delete() {
        val id = loadedCategory?.id ?: return
        viewModelScope.launch {
            when (val result = categoryRepository.deleteCategory(id)) {
                DeleteCategoryResult.Deleted -> backEvents.trySend(Unit)

                is DeleteCategoryResult.Blocked ->
                    draft.value = draft.value.copy(deleteBlockedCount = result.productCount)
            }
        }
    }

    companion object {
        private const val STOP_TIMEOUT = 5_000L
    }
}
