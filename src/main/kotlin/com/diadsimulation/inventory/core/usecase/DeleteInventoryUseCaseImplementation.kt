package com.diadsimulation.inventory.core.usecase

import com.diadsimulation.inventory.core.ports.input.DeleteInventoryInputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException
import java.util.UUID

open class DeleteInventoryUseCaseImplementation(
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
) : DeleteInventoryInputPort {
    @Transactional
    override fun execute(id: UUID) {
        inventoryRepositoryOutputPort.findById(id)
            ?: throw NoSuchElementException("Item do estoque não encontrado.")

        inventoryRepositoryOutputPort.deleteById(id)
    }
}
