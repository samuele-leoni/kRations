@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.example.krations.core.database

import androidx.room3.AutoMigration
import androidx.room3.Dao
import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.DeleteColumn
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.migration.AutoMigrationSpec
import com.example.krations.inventory.data.local.dao.CategoryDao
import com.example.krations.inventory.data.local.dao.InventoryBatchDao
import com.example.krations.inventory.data.local.dao.ProductDao
import com.example.krations.inventory.data.local.entity.CategoryEntity
import com.example.krations.inventory.data.local.entity.InventoryBatchEntity
import com.example.krations.inventory.data.local.entity.ProductEntity
import com.example.krations.shoppinglist.data.local.dao.ShoppingListItemDao
import com.example.krations.shoppinglist.data.local.entity.ShoppingListItemEntity

@Database(
    entities = [
        ShoppingListItemEntity::class,
        ProductEntity::class,
        CategoryEntity::class,
        InventoryBatchEntity::class,
    ],
    version = 4,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AppMigration2To3::class),
        AutoMigration(from = 3, to = 4),
    ],
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListItemDao(): ShoppingListItemDao

    abstract fun productDao(): ProductDao

    abstract fun categoryDao(): CategoryDao

    abstract fun inventoryBatchDao(): InventoryBatchDao

    abstract fun localDataOwnershipDao(): LocalDataOwnershipDao
}

@Dao
interface LocalDataOwnershipDao {
    @Query("UPDATE InventoryBatchEntity SET ownerUserId = :newOwnerUserId WHERE ownerUserId = :localOwnerUserId")
    suspend fun moveInventoryBatches(localOwnerUserId: String, newOwnerUserId: String)

    @Query("UPDATE ShoppingListItem SET ownerUserId = :newOwnerUserId WHERE ownerUserId = :localOwnerUserId")
    suspend fun moveShoppingListItems(localOwnerUserId: String, newOwnerUserId: String)

    @Query("UPDATE ProductEntity SET ownerUserId = :newOwnerUserId WHERE ownerUserId = :localOwnerUserId")
    suspend fun moveProducts(localOwnerUserId: String, newOwnerUserId: String)

    @Query("UPDATE CategoryEntity SET ownerUserId = :newOwnerUserId WHERE ownerUserId = :localOwnerUserId")
    suspend fun moveCategories(localOwnerUserId: String, newOwnerUserId: String)

    @Transaction
    suspend fun moveLocalDataTo(newOwnerUserId: String) {
        moveCategories(LOCAL_OWNER_USER_ID, newOwnerUserId)
        moveProducts(LOCAL_OWNER_USER_ID, newOwnerUserId)
        moveInventoryBatches(LOCAL_OWNER_USER_ID, newOwnerUserId)
        moveShoppingListItems(LOCAL_OWNER_USER_ID, newOwnerUserId)
    }

    private companion object {
        const val LOCAL_OWNER_USER_ID = "local-user"
    }
}

class LocalDataOwnershipMigrator(private val ownershipDao: LocalDataOwnershipDao) {
    suspend fun moveLocalDataTo(userId: String) {
        require(userId.isNotBlank()) { "Cannot assign local data to a blank user id" }
        if (userId == "local-user") return
        ownershipDao.moveLocalDataTo(userId)
    }
}

@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

@DeleteColumn(tableName = "ProductEntity", columnName = "quantity")
class AppMigration2To3 : AutoMigrationSpec
