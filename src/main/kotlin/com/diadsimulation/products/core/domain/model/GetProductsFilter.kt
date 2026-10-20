package com.diadsimulation.products.core.domain.model

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import java.math.BigDecimal
import java.time.OffsetDateTime

data class GetProductsFilter(
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val category: ProductCategory? = null,
    val active: Boolean? = null,
    val pricingType: PricingType? = null,
    val feeName: String? = null,
    val chargeType: FeeChargeType? = null,
    val minQuantity: Int? = null,
    val maxQuantity: Int? = null,
    val minShelfLifeDays: Int? = null,
    val maxShelfLifeDays: Int? = null,
    val minPrice: BigDecimal? = null,
    val maxPrice: BigDecimal? = null,
    val minFeeAmount: BigDecimal? = null,
    val maxFeeAmount: BigDecimal? = null,
    val hasFees: Boolean? = null,
    val createdFrom: OffsetDateTime? = null,
    val createdTo: OffsetDateTime? = null,
    val page: Int = 0,
    val size: Int = 20,
    val sortBy: String = "name",
    val sortDirection: String = "ASC",
)
