package com.diadsimulation.sales.core.domain.model

import java.util.UUID

data class SaleItem(
    val id: UUID = UUID.randomUUID(),
    val productId: UUID,
    val inventoryId: UUID? = null,
    val quantity: Int,
    val color: String? = null,
    val size: String? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
)
