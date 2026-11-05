package com.diadsimulation.inventory.core.domain.model

import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import java.util.UUID

data class UpdateInventory(
    val name: String? = null,
    val category: String? = null,
    val categoryId: UUID? = null,
    val description: String? = null,
    val quantity: Int? = null,
    val minimumQuantity: Int? = null,
    val unit: InventoryUnit? = null,
    val color: String? = null,
    val size: String? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
)
