package com.diadsimulation.sales.app.adapter.input.rest.dtos.responses

import java.math.BigDecimal
import java.util.UUID

data class CreateSaleItemResponse(
    val id: UUID,
    val productId: UUID,
    val inventoryId: UUID?,
    val quantity: Int,
    val unitPrice: BigDecimal,
    val additionalAmount: BigDecimal,
    val totalPrice: BigDecimal,
    val color: String?,
    val size: String?,
    val material: String?,
    val brand: String?,
    val model: String?,
)
