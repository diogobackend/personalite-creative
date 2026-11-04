package com.diadsimulation.categories.app.adapter.output.persistence.mappers

import com.diadsimulation.categories.app.adapter.output.persistence.entity.CategoryEntity
import com.diadsimulation.categories.core.domain.model.Category
import org.springframework.stereotype.Component

@Component
class CategoryPersistenceMapper {
    fun toEntity(category: Category): CategoryEntity =
        CategoryEntity(
            id = category.id,
            name = category.name,
            type = category.type,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt,
        )

    fun toDomain(entity: CategoryEntity): Category =
        Category(
            id = entity.id,
            name = entity.name,
            type = entity.type,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
}
