package com.diadsimulation.products.core.domain.model

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import com.diadsimulation.products.core.domain.enums.PricingType
import java.math.BigDecimal
import java.time.OffsetDateTime

data class GetProductsFilter(
    val code: String? = null,
    val name: String? = null,
    val description: String? = null,
    val category: String? = null,
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
    val colors: List<String>? = null,
    val sizes: List<String>? = null,
    val brands: List<String>? = null,
    val models: List<String>? = null,
    val materials: List<String>? = null,
    val page: Int = 0,
    val size: Int = 20,
    val sortBy: String = "name",
    val sortDirection: String = "ASC",
)
