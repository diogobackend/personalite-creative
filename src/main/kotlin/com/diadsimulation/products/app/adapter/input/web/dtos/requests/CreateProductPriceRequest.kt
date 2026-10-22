package com.diadsimulation.products.app.adapter.input.web.dtos.requests

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class CreateProductPriceRequest(
    @field:Positive
    val minimumQuantity: Int,
    @field:DecimalMin("0.01")
    val unitPrice: BigDecimal,
)
