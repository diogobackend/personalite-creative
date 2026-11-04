package com.diadsimulation.categories.core.ports.input

import com.diadsimulation.categories.core.domain.enums.CategoryType
import com.diadsimulation.categories.core.domain.model.Category

interface GetCategoriesInputPort {
    fun execute(
        name: String?,
        type: CategoryType?,
    ): List<Category>
}
