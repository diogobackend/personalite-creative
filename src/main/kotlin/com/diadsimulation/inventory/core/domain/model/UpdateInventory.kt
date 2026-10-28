package com.diadsimulation.inventory.core.domain.model

import com.diadsimulation.inventory.core.domain.enums.InventorySize
import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import com.diadsimulation.products.core.domain.enums.ProductCategory

data class UpdateInventory(
    val name: String? = null,
    val category: ProductCategory? = null,
    val description: String? = null,
    val quantity: Int? = null,
    val minimumQuantity: Int? = null,
    val unit: InventoryUnit? = null,
    val color: String? = null,
    val size: InventorySize? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
)
