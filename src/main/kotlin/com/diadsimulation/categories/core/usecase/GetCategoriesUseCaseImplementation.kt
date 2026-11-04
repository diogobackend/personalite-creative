package com.diadsimulation.categories.core.usecase

import com.diadsimulation.categories.core.domain.enums.CategoryType
import com.diadsimulation.categories.core.domain.model.Category
import com.diadsimulation.categories.core.ports.input.GetCategoriesInputPort
import com.diadsimulation.categories.core.ports.output.CategoryRepositoryOutputPort

class GetCategoriesUseCaseImplementation(
    private val categoryRepositoryOutputPort: CategoryRepositoryOutputPort,
) : GetCategoriesInputPort {
    override fun execute(
        name: String?,
        type: CategoryType?,
    ): List<Category> =
        categoryRepositoryOutputPort.findAll(
            name = name?.trim()?.takeIf { it.isNotEmpty() },
            type = type,
        )
}
