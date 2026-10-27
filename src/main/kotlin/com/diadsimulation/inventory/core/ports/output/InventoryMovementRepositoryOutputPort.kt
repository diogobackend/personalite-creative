package com.diadsimulation.inventory.core.ports.output

import com.diadsimulation.inventory.core.domain.model.InventoryMovement

interface InventoryMovementRepositoryOutputPort {
    fun save(movement: InventoryMovement): InventoryMovement
}
