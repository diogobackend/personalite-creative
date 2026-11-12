package com.diadsimulation.expenses.app.adapter.input.rest.dtos.responses

import java.math.BigDecimal
import java.util.UUID

data class ExpenseProductResponse(
    val productId: UUID,
    val allocatedValue: BigDecimal,
)
