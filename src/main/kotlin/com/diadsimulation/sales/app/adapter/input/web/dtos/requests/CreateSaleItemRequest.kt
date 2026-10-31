package com.diadsimulation.sales.app.adapter.input.web.dtos.requests

import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.util.UUID

data class CreateSaleItemRequest(
    val productId: UUID,
    val inventoryId: UUID? = null,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val additionalAmount: BigDecimal = ZERO,
    val color: String? = null,
    val size: String? = null,
    val material: String? = null,
    val brand: String? = null,
    val model: String? = null,
)
