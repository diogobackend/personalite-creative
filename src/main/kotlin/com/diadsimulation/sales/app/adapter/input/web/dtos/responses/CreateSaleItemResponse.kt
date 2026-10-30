package com.diadsimulation.sales.app.adapter.input.web.dtos.responses

import java.util.UUID

data class CreateSaleItemResponse(
    val id: UUID,
    val productId: UUID,
    val inventoryId: UUID?,
    val quantity: Int,
    val color: String?,
    val size: String?,
    val material: String?,
    val brand: String?,
    val model: String?,
)
