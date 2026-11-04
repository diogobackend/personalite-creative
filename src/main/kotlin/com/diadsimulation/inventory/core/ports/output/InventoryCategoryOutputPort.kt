package com.diadsimulation.inventory.core.ports.output

import java.util.UUID

interface InventoryCategoryOutputPort {
    fun existsByIdAndTypeInventory(categoryId: UUID): Boolean
}
