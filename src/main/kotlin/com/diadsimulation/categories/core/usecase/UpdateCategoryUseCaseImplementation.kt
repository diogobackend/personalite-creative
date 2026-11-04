package com.diadsimulation.categories.core.usecase

import com.diadsimulation.categories.core.domain.exceptions.CategoryAlreadyExistsException
import com.diadsimulation.categories.core.domain.model.Category
import com.diadsimulation.categories.core.ports.input.UpdateCategoryInputPort
import com.diadsimulation.categories.core.ports.output.CategoryRepositoryOutputPort
import java.time.LocalDateTime
import java.util.NoSuchElementException
import java.util.UUID

class UpdateCategoryUseCaseImplementation(
    private val categoryRepositoryOutputPort: CategoryRepositoryOutputPort,
) : UpdateCategoryInputPort {
    override fun execute(
        id: UUID,
        name: String,
    ): Category {
        val category =
            categoryRepositoryOutputPort.findById(id)
                ?: throw NoSuchElementException("Categoria não encontrada.")

        val normalizedName = name.trim()

        require(normalizedName.isNotBlank()) {
            "O nome da categoria é obrigatório."
        }

        require(normalizedName.length <= 150) {
            "O nome deve ter no máximo 150 caracteres."
        }

        if (category.name == normalizedName) {
            return category
        }

        if (categoryRepositoryOutputPort.existsByNameAndType(
                normalizedName,
                category.type,
            )
        ) {
            throw CategoryAlreadyExistsException()
        }

        return categoryRepositoryOutputPort.save(
            category.copy(
                name = normalizedName,
                updatedAt = LocalDateTime.now(),
            ),
        )
    }
}
