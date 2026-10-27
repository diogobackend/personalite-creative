package com.diadsimulation.inventory.core.domain.model

import java.time.OffsetDateTime
import java.util.UUID

data class Inventory(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val category: String,
    val description: String? = null,
    val productId: UUID? = null,
    val quantity: Int = 0,
    val minimumQuantity: Int? = null,
    val unit: String,
    val color: String? = null,
    val size: String? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val sku: String? = null,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
