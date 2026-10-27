package com.diadsimulation.inventory.core.usecase

import com.diadsimulation.inventory.core.domain.enums.InventoryMovementType.IN
import com.diadsimulation.inventory.core.domain.model.Inventory
import com.diadsimulation.inventory.core.domain.model.InventoryMovement
import com.diadsimulation.inventory.core.ports.input.RegisterInventoryInputPort
import com.diadsimulation.inventory.core.ports.output.InventoryMovementRepositoryOutputPort
import com.diadsimulation.inventory.core.ports.output.InventoryRepositoryOutputPort
import org.springframework.transaction.annotation.Transactional

open class RegisterInventoryUseCaseImplementation(
    private val inventoryRepositoryOutputPort: InventoryRepositoryOutputPort,
    private val inventoryMovementRepositoryOutputPort: InventoryMovementRepositoryOutputPort,
) : RegisterInventoryInputPort {
    @Transactional
    override fun execute(inventory: Inventory): Inventory {
        require(inventory.quantity >= 0) {
            "A quantidade inicial não pode ser negativa."
        }

        inventory.sku?.let { sku ->
            require(!inventoryRepositoryOutputPort.existsBySku(sku)) {
                "Já existe um item no estoque com este SKU."
            }
        }

        val registeredInventory = inventoryRepositoryOutputPort.save(inventory)

        if (registeredInventory.quantity > 0) {
            val movement =
                InventoryMovement(
                    inventoryId = registeredInventory.id,
                    type = IN,
                    quantity = registeredInventory.quantity,
                    reason = "INITIAL_STOCK",
                )

            inventoryMovementRepositoryOutputPort.save(movement)
        }

        return registeredInventory
    }
}
