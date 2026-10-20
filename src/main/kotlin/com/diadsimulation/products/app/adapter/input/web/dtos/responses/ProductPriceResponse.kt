package com.diadsimulation.products.app.adapter.input.web.dtos.responses

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class ProductPriceResponse(
    val id: UUID,
    val minimumQuantity: Int,
    val unitPrice: BigDecimal,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
