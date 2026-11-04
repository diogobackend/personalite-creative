package com.diadsimulation.categories.core.usecase

import com.diadsimulation.categories.core.ports.input.DeleteCategoryInputPort
import com.diadsimulation.categories.core.ports.output.CategoryRepositoryOutputPort
import java.util.NoSuchElementException
import java.util.UUID

class DeleteCategoryUseCaseImplementation(
    private val categoryRepositoryOutputPort: CategoryRepositoryOutputPort,
) : DeleteCategoryInputPort {
    override fun execute(id: UUID) {
        categoryRepositoryOutputPort.findById(id)
            ?: throw NoSuchElementException("Categoria não encontrada.")

        categoryRepositoryOutputPort.deleteById(id)
    }
}
