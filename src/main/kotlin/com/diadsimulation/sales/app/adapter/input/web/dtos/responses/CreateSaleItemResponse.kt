package com.diadsimulation.sales.app.adapter.input.web.dtos.responses

import java.util.UUID

data class CreateSaleItemResponse(
    val id: UUID,
    val productId: UUID,
    val inventoryId: UUID?,
)
