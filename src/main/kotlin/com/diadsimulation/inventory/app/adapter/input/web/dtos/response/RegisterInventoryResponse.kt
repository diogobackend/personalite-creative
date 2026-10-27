package com.diadsimulation.inventory.app.adapter.input.web.dtos.response

import com.diadsimulation.inventory.core.domain.enums.InventorySize
import com.diadsimulation.inventory.core.domain.enums.InventoryUnit
import com.diadsimulation.products.core.domain.enums.ProductCategory
import java.time.OffsetDateTime
import java.util.UUID

data class RegisterInventoryResponse(
    val id: UUID,
    val name: String,
    val category: ProductCategory,
    val description: String?,
    val productId: UUID?,
    val quantity: Int,
    val minimumQuantity: Int?,
    val unit: InventoryUnit?,
    val color: String?,
    val size: InventorySize?,
    val material: String?,
    val brand: String?,
    val model: String?,
    val sku: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
