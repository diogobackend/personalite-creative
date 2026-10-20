package com.diadsimulation.products.core.domain.model

import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import com.diadsimulation.products.core.domain.model.ProductFee
import com.diadsimulation.products.core.domain.model.ProductPrice
import java.time.OffsetDateTime
import java.util.Collections
import java.util.UUID

data class Product(
    val id: UUID = UUID.randomUUID(),
    val code: String,
    val name: String,
    val category: ProductCategory,
    val description: String? = null,
    val minimumQuantity: Int? = null,
    val pricingType: PricingType,
    val shelfLifeDays: Int? = null,
    val productionLeadTimeMinDays: Int? = null,
    val productionLeadTimeMaxDays: Int? = null,
    val active: Boolean = true,
    val prices: List<ProductPrice> = Collections.emptyList(),
    val fees: List<ProductFee> = Collections.emptyList(),
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
