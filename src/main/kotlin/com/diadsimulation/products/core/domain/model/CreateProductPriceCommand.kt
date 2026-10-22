package com.diadsimulation.products.core.domain.model

import java.math.BigDecimal

data class CreateProductPriceCommand(
    val minimumQuantity: Int,
    val unitPrice: BigDecimal,
)
