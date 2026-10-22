package com.diadsimulation.products.core.domain.model

import com.diadsimulation.products.core.domain.enums.FeeChargeType
import java.math.BigDecimal

data class CreateProductFeeCommand(
    val name: String,
    val amount: BigDecimal,
    val chargeType: FeeChargeType,
    val required: Boolean = false,
    val active: Boolean = true,
)
