package com.example.krations.core.data

import com.example.krations.inventory.data.local.dao.CategoryDao
import com.example.krations.inventory.data.local.dao.InventoryBatchDao
import com.example.krations.inventory.data.local.dao.ProductDao
import com.example.krations.inventory.data.local.entity.CategoryEntity
import com.example.krations.inventory.data.local.entity.InventoryBatchEntity
import com.example.krations.inventory.data.local.entity.ProductEntity
import com.example.krations.shoppinglist.data.local.dao.ShoppingListItemDao
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItemEntity
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class SupabaseSyncEngine(
    private val supabase: SupabaseClient,
    private val categoryDao: CategoryDao,
    private val productDao: ProductDao,
    private val inventoryBatchDao: InventoryBatchDao,
    private val shoppingListDao: ShoppingListItemDao,
) {
    private val mutex = Mutex()

    suspend fun synchronize(ownerUserId: String) = mutex.withLock {
        require(ownerUserId.isNotBlank() && ownerUserId != LOCAL_USER_ID) {
            "A signed-in user is required to synchronize"
        }
        syncCategories(ownerUserId)
        syncProducts(ownerUserId)
        syncInventoryBatches(ownerUserId)
        syncShoppingListItems(ownerUserId)
    }

    private suspend fun syncCategories(ownerUserId: String) {
        val local = categoryDao.getRowsForSync(ownerUserId).associateBy { it.id }
        val remote = supabase.from("categories").select {
            filter { eq("owner_user_id", ownerUserId) }
        }.decodeList<CategoryRecord>().associateBy { it.id }
        val localWrites = mutableListOf<CategoryEntity>()
        val remoteWrites = mutableListOf<CategoryRecord>()

        (local.keys + remote.keys).forEach { id ->
            val localRow = local[id]
            val remoteRow = remote[id]
            when {
                localRow == null -> localWrites += remoteRow!!.toEntity()
                remoteRow == null -> remoteWrites += localRow.toRecord()
                isNewer(localRow.updatedAt, remoteRow.updatedAt) -> remoteWrites += localRow.toRecord()
                else -> localWrites += remoteRow.toEntity()
            }
        }
        if (localWrites.isNotEmpty()) categoryDao.upsertRowsForSync(localWrites)
        if (remoteWrites.isNotEmpty()) supabase.from("categories").upsert(remoteWrites)
    }

    private suspend fun syncProducts(ownerUserId: String) {
        val local = productDao.getRowsForSync(ownerUserId).associateBy { it.id }
        val remote = supabase.from("products").select {
            filter { eq("owner_user_id", ownerUserId) }
        }.decodeList<ProductRecord>().associateBy { it.id }
        val localWrites = mutableListOf<ProductEntity>()
        val remoteWrites = mutableListOf<ProductRecord>()

        (local.keys + remote.keys).forEach { id ->
            val localRow = local[id]
            val remoteRow = remote[id]
            when {
                localRow == null -> localWrites += remoteRow!!.toEntity()
                remoteRow == null -> remoteWrites += localRow.toRecord()
                isNewer(localRow.updatedAt, remoteRow.updatedAt) -> remoteWrites += localRow.toRecord()
                else -> localWrites += remoteRow.toEntity()
            }
        }
        if (localWrites.isNotEmpty()) productDao.upsertRowsForSync(localWrites)
        if (remoteWrites.isNotEmpty()) supabase.from("products").upsert(remoteWrites)
    }

    private suspend fun syncInventoryBatches(ownerUserId: String) {
        val local = inventoryBatchDao.getRowsForSync(ownerUserId).associateBy { it.id }
        val remote = supabase.from("inventory_batches").select {
            filter { eq("owner_user_id", ownerUserId) }
        }.decodeList<InventoryBatchRecord>().associateBy { it.id }
        val localWrites = mutableListOf<InventoryBatchEntity>()
        val remoteWrites = mutableListOf<InventoryBatchRecord>()

        (local.keys + remote.keys).forEach { id ->
            val localRow = local[id]
            val remoteRow = remote[id]
            when {
                localRow == null -> localWrites += remoteRow!!.toEntity()
                remoteRow == null -> remoteWrites += localRow.toRecord()
                isNewer(localRow.updatedAt, remoteRow.updatedAt) -> remoteWrites += localRow.toRecord()
                else -> localWrites += remoteRow.toEntity()
            }
        }
        if (localWrites.isNotEmpty()) inventoryBatchDao.upsertRowsForSync(localWrites)
        if (remoteWrites.isNotEmpty()) supabase.from("inventory_batches").upsert(remoteWrites)
    }

    private suspend fun syncShoppingListItems(ownerUserId: String) {
        val local = shoppingListDao.getRowsForSync(ownerUserId).associateBy { it.id }
        val remote = supabase.from("shopping_list_items").select {
            filter { eq("owner_user_id", ownerUserId) }
        }.decodeList<ShoppingListItemRecord>().associateBy { it.id }
        val localWrites = mutableListOf<ShoppingListItemEntity>()
        val remoteWrites = mutableListOf<ShoppingListItemRecord>()

        (local.keys + remote.keys).forEach { id ->
            val localRow = local[id]
            val remoteRow = remote[id]
            when {
                localRow == null -> localWrites += remoteRow!!.toEntity()
                remoteRow == null -> remoteWrites += localRow.toRecord()
                isNewer(localRow.updatedAt, remoteRow.updatedAt) -> remoteWrites += localRow.toRecord()
                else -> localWrites += remoteRow.toEntity()
            }
        }
        if (localWrites.isNotEmpty()) shoppingListDao.upsertRowsForSync(localWrites)
        if (remoteWrites.isNotEmpty()) supabase.from("shopping_list_items").upsert(remoteWrites)
    }

    private fun isNewer(localUpdatedAt: String, remoteUpdatedAt: String): Boolean =
        runCatching { Instant.parse(localUpdatedAt) > Instant.parse(remoteUpdatedAt) }
            .getOrElse { localUpdatedAt > remoteUpdatedAt }

    @Serializable
    private data class CategoryRecord(
        val id: String,
        @SerialName("owner_user_id") val ownerUserId: String,
        val name: String,
        @SerialName("icon_name") val iconName: String,
        @SerialName("created_at") val createdAt: String,
        @SerialName("updated_at") val updatedAt: String,
        @SerialName("deleted_at") val deletedAt: String?,
    ) {
        fun toEntity() = CategoryEntity(id, ownerUserId, name, iconName, createdAt, updatedAt, deletedAt)
    }

    @Serializable
    private data class ProductRecord(
        val id: String,
        @SerialName("owner_user_id") val ownerUserId: String,
        @SerialName("category_id") val categoryId: String?,
        val name: String,
        val description: String?,
        @SerialName("icon_name") val iconName: String?,
        val unit: String,
        @SerialName("min_quantity") val minQuantity: Double,
        @SerialName("expiry_alert_days") val expiryAlertDays: Int,
        @SerialName("created_at") val createdAt: String,
        @SerialName("updated_at") val updatedAt: String,
        @SerialName("deleted_at") val deletedAt: String?,
    ) {
        fun toEntity() = ProductEntity(
            id, ownerUserId, categoryId, name, description, iconName, unit, minQuantity,
            expiryAlertDays, createdAt, updatedAt, deletedAt,
        )
    }

    @Serializable
    private data class InventoryBatchRecord(
        val id: String,
        @SerialName("owner_user_id") val ownerUserId: String,
        @SerialName("product_id") val productId: String,
        val quantity: Double,
        @SerialName("expiration_date") val expirationDate: String?,
        @SerialName("created_at") val createdAt: String,
        @SerialName("updated_at") val updatedAt: String,
        @SerialName("deleted_at") val deletedAt: String?,
    ) {
        fun toEntity() = InventoryBatchEntity(
            id, ownerUserId, productId, quantity, expirationDate, createdAt, updatedAt, deletedAt,
        )
    }

    @Serializable
    private data class ShoppingListItemRecord(
        val id: String,
        @SerialName("owner_user_id") val ownerUserId: String,
        @SerialName("product_id") val productId: String?,
        val name: String,
        @SerialName("quantity_needed") val quantityNeeded: Double,
        val unit: String,
        @SerialName("is_purchased") val isPurchased: Boolean,
        @SerialName("purchased_quantity") val purchasedQuantity: Double,
        @SerialName("purchased_at") val purchasedAt: String?,
        @SerialName("created_at") val createdAt: String,
        @SerialName("updated_at") val updatedAt: String,
        @SerialName("deleted_at") val deletedAt: String?,
    ) {
        fun toEntity() = ShoppingListItemEntity(
            id, ownerUserId, productId, name, quantityNeeded, unit, isPurchased,
            purchasedQuantity, purchasedAt, createdAt, updatedAt, deletedAt,
        )
    }

    private fun CategoryEntity.toRecord() =
        CategoryRecord(id, ownerUserId, name, iconName, createdAt, updatedAt, deletedAt)

    private fun ProductEntity.toRecord() =
        ProductRecord(
            id, ownerUserId, categoryId, name, description, iconName, unit,
            minQuantity, expiryAlertDays, createdAt, updatedAt, deletedAt,
        )

    private fun InventoryBatchEntity.toRecord() =
        InventoryBatchRecord(
            id, ownerUserId, productId, quantity, expirationDate, createdAt, updatedAt, deletedAt,
        )

    private fun ShoppingListItemEntity.toRecord() =
        ShoppingListItemRecord(
            id, ownerUserId, productId, name, quantityNeeded, unit, isPurchased,
            purchasedQuantity, purchasedAt, createdAt, updatedAt, deletedAt,
        )
}
