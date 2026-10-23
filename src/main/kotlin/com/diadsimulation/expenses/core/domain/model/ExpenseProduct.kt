package com.diadsimulation.expenses.core.domain.model

import java.math.BigDecimal
import java.util.UUID

data class ExpenseProduct(
    val productId: UUID,
    val allocatedValue: BigDecimal,
)
