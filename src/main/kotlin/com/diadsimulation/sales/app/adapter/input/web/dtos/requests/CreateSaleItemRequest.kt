package com.diadsimulation.sales.app.adapter.input.web.dtos.requests

import java.util.UUID

data class CreateSaleItemRequest(
    val productId: UUID,
    val inventoryId: UUID? = null,
)
