package com.diadsimulation.categories.app.adapter.input.web.mappers

import com.diadsimulation.categories.app.adapter.input.web.dtos.responses.CategoryResponse
import com.diadsimulation.categories.core.domain.model.Category
import org.springframework.stereotype.Component

@Component
class CategoryWebMapper {
    fun toResponse(category: Category): CategoryResponse =
        CategoryResponse(
            id = category.id,
            name = category.name,
            type = category.type,
            createdAt = category.createdAt,
            updatedAt = category.updatedAt,
        )
}
