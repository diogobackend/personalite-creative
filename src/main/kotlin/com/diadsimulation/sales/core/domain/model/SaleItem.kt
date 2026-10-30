package com.diadsimulation.sales.core.domain.model

import java.util.UUID

data class SaleItem(
    val id: UUID = UUID.randomUUID(),
    val productId: UUID,
    val inventoryId: UUID? = null,
)
