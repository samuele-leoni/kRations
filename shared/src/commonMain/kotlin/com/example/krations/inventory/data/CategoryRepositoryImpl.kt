package com.example.krations.inventory.data

import com.example.krations.core.data.CurrentUserProvider
import com.example.krations.inventory.data.local.dao.CategoryDao
import com.example.krations.inventory.data.local.entity.CategoryEntity
import com.example.krations.inventory.data.local.entity.CategoryWithCount
import com.example.krations.inventory.domain.CategoryRepository
import com.example.krations.inventory.domain.DeleteCategoryResult
import com.example.krations.inventory.domain.model.Category
import com.example.krations.inventory.domain.model.CategorySummary
import kotlin.time.Clock.System.now
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val currentUserProvider: CurrentUserProvider,
) : CategoryRepository {

    override fun observeCategories(): Flow<List<Category>> =
        currentUserProvider.userId.flatMapLatest { userId ->
            categoryDao.getCategories(userId).map { rows -> rows.map(CategoryEntity::toCategory) }
        }

    override fun observeCategoriesWithCount(): Flow<List<CategorySummary>> =
        currentUserProvider.userId.flatMapLatest { userId ->
            categoryDao.getCategoriesWithCount(userId).map { rows -> rows.map(CategoryWithCount::toSummary) }
        }

    override suspend fun getCategory(id: String): Category? =
        categoryDao.getCategoryById(id, currentUserProvider.userId.value)?.toCategory()

    override suspend fun addCategory(name: String, iconName: String) =
        now().toString().let { timestamp ->
            categoryDao.insertCategory(
                CategoryEntity(
                    id = Uuid.random().toString(),
                    ownerUserId = currentUserProvider.userId.value,
                    name = name,
                    iconName = iconName,
                    createdAt = timestamp,
                    updatedAt = timestamp,
                    deletedAt = null,
                ),
            )
        }

    override suspend fun updateCategory(category: Category) {
        val userId = currentUserProvider.userId.value
        val existing = categoryDao.getCategoryById(category.id, userId) ?: return
        categoryDao.updateCategory(
            existing.copy(
                name = category.name,
                iconName = category.iconName,
                updatedAt = now().toString(),
            ),
        )
    }

    override suspend fun deleteCategory(id: String): DeleteCategoryResult {
        val userId = currentUserProvider.userId.value
        val attached = categoryDao.softDeleteIfUnused(
            deletedAt = now().toString(),
            id = id,
            ownerUserId = userId,
        )

        return if (attached > 0) {
            DeleteCategoryResult.Blocked(attached)
        } else {
            DeleteCategoryResult.Deleted
        }
    }
}
