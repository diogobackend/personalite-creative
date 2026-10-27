package com.diadsimulation.inventory.core.ports.input

import com.diadsimulation.inventory.core.domain.model.Inventory

interface RegisterInventoryInputPort {
    fun execute(inventory: Inventory): Inventory
}
