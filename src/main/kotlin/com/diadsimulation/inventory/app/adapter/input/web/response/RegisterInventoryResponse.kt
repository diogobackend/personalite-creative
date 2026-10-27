package com.diadsimulation.inventory.app.adapter.input.web.response

import java.time.OffsetDateTime
import java.util.UUID

data class RegisterInventoryResponse(
    val id: UUID,
    val name: String,
    val category: String,
    val description: String?,
    val productId: UUID?,
    val quantity: Int,
    val minimumQuantity: Int?,
    val unit: String?,
    val color: String?,
    val size: String?,
    val material: String?,
    val brand: String?,
    val model: String?,
    val sku: String?,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
