package com.example.krations.inventory.data

import com.example.krations.core.data.LOCAL_USER_ID
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
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override fun observeCategories(): Flow<List<Category>> =
        categoryDao.getCategories(LOCAL_USER_ID).map { rows -> rows.map(CategoryEntity::toCategory) }

    override fun observeCategoriesWithCount(): Flow<List<CategorySummary>> =
        categoryDao.getCategoriesWithCount(LOCAL_USER_ID).map { rows -> rows.map(CategoryWithCount::toSummary) }

    override suspend fun getCategory(id: String): Category? =
        categoryDao.getCategoryById(id, LOCAL_USER_ID)?.toCategory()

    override suspend fun addCategory(name: String, iconName: String) =
        now().toString().let { timestamp ->
            categoryDao.insertCategory(
                CategoryEntity(
                    id = Uuid.random().toString(),
                    ownerUserId = LOCAL_USER_ID,
                    name = name,
                    iconName = iconName,
                    createdAt = timestamp,
                    updatedAt = timestamp,
                    deletedAt = null,
                ),
            )
        }

    override suspend fun updateCategory(category: Category) {
        val existing = categoryDao.getCategoryById(category.id, LOCAL_USER_ID) ?: return
        categoryDao.updateCategory(
            existing.copy(
                name = category.name,
                iconName = category.iconName,
                updatedAt = now().toString(),
            ),
        )
    }

    override suspend fun deleteCategory(id: String): DeleteCategoryResult {
        val attached = categoryDao.softDeleteIfUnused(
            deletedAt = now().toString(),
            id = id,
            ownerUserId = LOCAL_USER_ID,
        )

        return if (attached > 0) {
            DeleteCategoryResult.Blocked(attached)
        } else {
            DeleteCategoryResult.Deleted
        }
    }
}
