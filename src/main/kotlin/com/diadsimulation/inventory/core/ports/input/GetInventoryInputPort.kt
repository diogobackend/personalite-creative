package com.diadsimulation.inventory.core.ports.input

import com.diadsimulation.inventory.core.domain.model.GetInventoryFilter
import com.diadsimulation.inventory.core.domain.model.InventoryPage

interface GetInventoryInputPort {
    fun execute(filter: GetInventoryFilter): InventoryPage
}
