package com.diadsimulation.inventory.core.ports.input

import com.diadsimulation.inventory.core.domain.model.Inventory
import java.util.UUID

fun interface GetInventoryByIdInputPort {
    fun execute(id: UUID): Inventory
}
