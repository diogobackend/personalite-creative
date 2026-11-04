package com.diadsimulation.categories.core.ports.output

import com.diadsimulation.categories.core.domain.enums.CategoryType
import com.diadsimulation.categories.core.domain.model.Category

interface CategoryRepositoryOutputPort {
    fun save(category: Category): Category

    fun existsByNameAndType(
        name: String,
        type: CategoryType,
    ): Boolean
}
