package com.diadsimulation.inventory.core.ports.input

import com.diadsimulation.inventory.core.domain.model.Inventory
import com.diadsimulation.inventory.core.domain.model.UpdateInventory
import java.util.UUID

fun interface UpdateInventoryInputPort {
    fun execute(
        id: UUID,
        update: UpdateInventory,
    ): Inventory
}
