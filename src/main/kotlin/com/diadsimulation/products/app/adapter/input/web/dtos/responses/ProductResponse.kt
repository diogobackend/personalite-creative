package com.diadsimulation.products.app.adapter.input.web.dtos.responses

import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import java.time.OffsetDateTime
import java.util.UUID

data class ProductResponse(
    val id: UUID,
    val code: String,
    val name: String,
    val category: ProductCategory,
    val description: String?,
    val minimumQuantity: Int?,
    val pricingType: PricingType,
    val shelfLifeDays: Int?,
    val productionLeadTimeMinDays: Int?,
    val productionLeadTimeMaxDays: Int?,
    val active: Boolean,
    val prices: List<ProductPriceResponse>,
    val fees: List<ProductFeeResponse>,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
