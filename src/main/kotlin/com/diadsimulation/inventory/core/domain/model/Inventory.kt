package com.diadsimulation.inventory.core.domain.model

import com.diadsimulation.inventory.core.domain.enums.InventorySize
import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import com.diadsimulation.products.core.domain.enums.ProductCategory
import java.time.OffsetDateTime
import java.util.UUID

data class Inventory(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val category: ProductCategory,
    val description: String? = null,
    val productId: UUID? = null,
    val quantity: Int = 0,
    val minimumQuantity: Int? = null,
    val unit: InventoryUnit? = null,
    val color: String? = null,
    val size: InventorySize? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val sku: String? = null,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
