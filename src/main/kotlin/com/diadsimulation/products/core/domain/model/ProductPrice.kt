package com.diadsimulation.products.core.domain.model

import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class ProductPrice(
    val id: UUID = UUID.randomUUID(),
    val minimumQuantity: Int,
    val unitPrice: BigDecimal,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
