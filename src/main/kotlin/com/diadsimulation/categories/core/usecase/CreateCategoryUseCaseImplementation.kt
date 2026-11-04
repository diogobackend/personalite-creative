package com.diadsimulation.categories.core.usecase

import com.diadsimulation.categories.core.domain.enums.CategoryType
import com.diadsimulation.categories.core.domain.exceptions.CategoryAlreadyExistsException
import com.diadsimulation.categories.core.domain.model.Category
import com.diadsimulation.categories.core.ports.input.CreateCategoryInputPort
import com.diadsimulation.categories.core.ports.output.CategoryRepositoryOutputPort
import java.time.LocalDateTime
import java.util.UUID

class CreateCategoryUseCaseImplementation(
    private val categoryRepositoryOutputPort: CategoryRepositoryOutputPort,
) : CreateCategoryInputPort {
    override fun execute(
        name: String,
        type: CategoryType,
    ): Category {
        val normalizedName = name.trim()

        require(normalizedName.isNotBlank()) {
            "O nome da categoria é obrigatório."
        }

        require(normalizedName.length <= 150) {
            "O nome da categoria deve ter no máximo 150 caracteres."
        }

        if (categoryRepositoryOutputPort.existsByNameAndType(normalizedName, type)) {
            throw CategoryAlreadyExistsException()
        }

        val now = LocalDateTime.now()

        return categoryRepositoryOutputPort.save(
            Category(
                id = UUID.randomUUID(),
                name = normalizedName,
                type = type,
                createdAt = now,
                updatedAt = now,
            ),
        )
    }
}
