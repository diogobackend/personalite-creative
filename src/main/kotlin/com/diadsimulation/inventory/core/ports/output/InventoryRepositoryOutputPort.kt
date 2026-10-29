package com.diadsimulation.inventory.core.ports.output

import com.diadsimulation.inventory.core.domain.model.GetInventoryFilter
import com.diadsimulation.inventory.core.domain.model.Inventory
import com.diadsimulation.inventory.core.domain.model.InventoryPage
import java.util.UUID

interface InventoryRepositoryOutputPort {
    fun save(inventory: Inventory): Inventory

    fun findById(inventoryId: UUID): Inventory?

    fun existsBySku(sku: String): Boolean

    fun findAll(filter: GetInventoryFilter): InventoryPage

    fun deleteById(inventoryId: UUID)
}
