package com.example.krations.inventory.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.krations.inventory.domain.CategoryRepository
import com.example.krations.inventory.domain.InventoryBatchRepository
import com.example.krations.inventory.domain.ProductRepository
import com.example.krations.inventory.domain.model.InventoryBatch
import com.example.krations.inventory.domain.model.Product
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductEditViewModel(
    private val productRepository: ProductRepository,
    private val batchRepository: InventoryBatchRepository,
    categoryRepository: CategoryRepository,
) : ViewModel() {

    private val draft = MutableStateFlow(ProductEditUiState())

    private var loadedProduct: Product? = null
    private var storedQuantities: Map<String, Double> = emptyMap()

    private val backEvents = Channel<Unit>(Channel.CONFLATED)

    val navigateBackEvent = backEvents.receiveAsFlow()

    val uiState: StateFlow<ProductEditUiState> =
        combine(draft, categoryRepository.observeCategories()) { state, categories ->
            state.copy(categories = categories)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT),
            initialValue = ProductEditUiState(),
        )

    fun load(productId: String?) {
        while (backEvents.tryReceive().isSuccess) {
            // A back event queued but never consumed must not pop the screen we are opening now.
        }

        // One view model backs both the AddProduct and the EditProduct route, and the fields render
        // before the reads below complete: without dropping the previous product here, Save or
        // Delete pressed during that window would write to the product of the screen that was open
        // before instead of the one the user is looking at now.
        loadedProduct = null
        storedQuantities = emptyMap()
        draft.value = ProductEditUiState(isCreate = productId == null, isLoading = true)

        viewModelScope.launch {
            val product = productId?.let { productRepository.getProduct(it) }
            val batches = productId?.let { batchRepository.observeBatches(it).first() }.orEmpty()

            loadedProduct = product
            storedQuantities = batches.associate { it.id to it.quantity }

            draft.value = if (product == null) {
                ProductEditUiState(isCreate = true)
            } else {
                ProductEditUiState(
                    isCreate = false,
                    name = product.name,
                    unit = product.unit,
                    categoryId = product.categoryId,
                    minQuantity = product.minQuantity,
                    expiryAlertDays = product.expiryAlertDays,
                    batches = batches.map { it.asDraft() },
                )
            }
        }
    }

    fun onNameChange(value: String) {
        draft.value = draft.value.copy(name = value)
    }

    fun onUnitChange(value: String) {
        draft.value = draft.value.copy(unit = value)
    }

    fun onCategoryChange(value: String?) {
        draft.value = draft.value.copy(categoryId = value)
    }

    fun onMinQuantityChange(value: Double) {
        draft.value = draft.value.copy(minQuantity = value.coerceAtLeast(0.0))
    }

    fun onExpiryAlertDaysChange(value: Int) {
        draft.value = draft.value.copy(expiryAlertDays = value.coerceAtLeast(0))
    }

    fun addBatch() {
        draft.value = draft.value.copy(batches = draft.value.batches + BatchDraft())
    }

    fun onBatchQuantityChange(index: Int, delta: Int) {
        updateBatchAt(index) { it.copy(quantity = (it.quantity + delta).coerceAtLeast(0)) }
    }

    fun onBatchExpirationChange(index: Int, expirationDate: String?) {
        updateBatchAt(index) { it.copy(expirationDate = expirationDate) }
    }

    fun save() {
        val state = draft.value
        if (!state.canSave) return
        val name = state.name.trim()

        viewModelScope.launch {
            val existing = loadedProduct
            val productId = if (existing == null) {
                productRepository.addProduct(
                    name = name,
                    description = null,
                    unit = state.unit,
                    categoryId = state.categoryId,
                    minQuantity = state.minQuantity,
                    expiryAlertDays = state.expiryAlertDays,
                )
            } else {
                productRepository.updateProduct(
                    existing.copy(
                        name = name,
                        unit = state.unit,
                        categoryId = state.categoryId,
                        minQuantity = state.minQuantity,
                        expiryAlertDays = state.expiryAlertDays,
                    ),
                )
                existing.id
            }

            state.batches.forEach { batch -> applyBatch(productId, batch) }

            backEvents.trySend(Unit)
        }
    }

    fun delete() {
        val id = loadedProduct?.id ?: return
        viewModelScope.launch {
            productRepository.deleteProduct(id)
            backEvents.trySend(Unit)
        }
    }

    private suspend fun applyBatch(productId: String, batch: BatchDraft) {
        val storedId = batch.batchId
        if (storedId == null) {
            if (batch.quantity > 0) {
                batchRepository.addBatch(productId, batch.quantity.toDouble(), batch.expirationDate)
            }
            return
        }

        if (batch.quantity == 0) {
            batchRepository.deleteBatch(storedId)
            return
        }

        if (storedQuantities[storedId] != batch.quantity.toDouble()) {
            batchRepository.setQuantity(storedId, batch.quantity.toDouble())
        }
    }

    private inline fun updateBatchAt(index: Int, change: (BatchDraft) -> BatchDraft) {
        val batches = draft.value.batches
        if (index !in batches.indices) return
        draft.value = draft.value.copy(batches = batches.toMutableList().also { it[index] = change(it[index]) })
    }

    private fun InventoryBatch.asDraft(): BatchDraft =
        BatchDraft(batchId = id, quantity = quantity.toInt(), expirationDate = expirationDate)

    companion object {
        private const val STOP_TIMEOUT = 5_000L
    }
}
