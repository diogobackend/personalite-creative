package com.diadsimulation.inventory.core.usecase

import com.diadsimulation.inventory.core.domain.model.Inventory
import com.diadsimulation.inventory.core.ports.input.GetInventoryByIdInputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import org.springframework.transaction.annotation.Transactional
import java.util.NoSuchElementException
import java.util.UUID

open class GetInventoryByIdUseCaseImplementation(
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
) : GetInventoryByIdInputPort {
    @Transactional(readOnly = true)
    override fun execute(id: UUID): Inventory =
        inventoryRepositoryOutputPort.findById(id)
            ?: throw NoSuchElementException("Item do estoque não encontrado.")
}
