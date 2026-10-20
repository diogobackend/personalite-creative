package com.diadsimulation.products.core.domain.model

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class ProductFee(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val amount: BigDecimal,
    val chargeType: FeeChargeType,
    val required: Boolean = false,
    val active: Boolean = true,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
