package com.diadsimulation.products.app.adapter.input.web.dtos.requests

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import com.diadsimulation.products.core.domain.enums.PricingType
import com.diadsimulation.products.core.domain.enums.ProductCategory
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal
import java.util.Collections.emptyList

data class CreateProductRequest(
    @field:NotBlank
    @field:Pattern(regexp = "[0-9]{3}")
    val code: String,
    @field:NotBlank
    val name: String,
    @field:NotNull
    val category: ProductCategory,
    val description: String? = null,
    @field:Positive
    val minimumQuantity: Int? = null,
    @field:NotNull
    val pricingType: PricingType,
    @field:Positive
    val shelfLifeDays: Int? = null,
    @field:PositiveOrZero
    val productionLeadTimeMinDays: Int? = null,
    @field:PositiveOrZero
    val productionLeadTimeMaxDays: Int? = null,
    val active: Boolean = true,
    @field:Valid
    val prices: List<CreateProductPriceRequest> = emptyList(),
    @field:Valid
    val fees: List<CreateProductFeeRequest> = emptyList(),
)

data class CreateProductPriceRequest(
    @field:Positive
    val minimumQuantity: Int,
    @field:DecimalMin("0.01")
    val unitPrice: BigDecimal,
)

data class CreateProductFeeRequest(
    @field:NotBlank
    val name: String,
    @field:DecimalMin("0.00")
    val amount: BigDecimal,
    @field:NotNull
    val chargeType: FeeChargeType,
    val required: Boolean = false,
    val active: Boolean = true,
)
