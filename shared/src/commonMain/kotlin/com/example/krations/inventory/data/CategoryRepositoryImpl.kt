package com.example.krations.inventory.data

import com.example.krations.inventory.data.local.dao.CategoryDao
import com.example.krations.inventory.data.local.entity.CategoryEntity
import com.example.krations.inventory.domain.CategoryRepository
import com.example.krations.inventory.domain.model.Category
import kotlin.time.Clock.System.now
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
) : CategoryRepository {

    override fun observeCategories(): Flow<List<Category>> =
        categoryDao.getCategories(LOCAL_USER_ID).map { rows -> rows.map(CategoryEntity::toCategory) }

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

    override suspend fun deleteCategory(id: String) = now().toString().let { timestamp ->
        categoryDao.deleteCategory(
            deletedAt = timestamp,
            updatedAt = timestamp,
            id = id,
            ownerUserId = LOCAL_USER_ID,
        )
    }
}

// TODO: get real user from account data
private const val LOCAL_USER_ID = "local-user"
