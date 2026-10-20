package com.diadsimulation.products.app.adapter.input.web.dtos.responses

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import java.math.BigDecimal
import java.time.OffsetDateTime
import java.util.UUID

data class ProductFeeResponse(
    val id: UUID,
    val name: String,
    val amount: BigDecimal,
    val chargeType: FeeChargeType,
    val required: Boolean,
    val active: Boolean,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)