package com.diadsimulation.categories.core.ports.output

import com.diadsimulation.categories.core.domain.enums.CategoryType
import com.diadsimulation.categories.core.domain.model.Category
import java.util.UUID

interface CategoryRepositoryOutputPort {
    fun save(category: Category): Category

    fun existsByNameAndType(
        name: String,
        type: CategoryType,
    ): Boolean

    fun findAll(
        name: String?,
        type: CategoryType?,
    ): List<Category>

    fun findById(id: UUID): Category?

    fun deleteById(id: UUID)
}
