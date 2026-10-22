package com.diadsimulation.products.app.adapter.input.web.dtos.requests

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

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
