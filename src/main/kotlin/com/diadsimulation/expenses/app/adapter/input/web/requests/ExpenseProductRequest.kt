package com.diadsimulation.expenses.app.adapter.input.web.requests

import jakarta.validation.constraints.DecimalMin
import java.math.BigDecimal
import java.util.UUID

data class ExpenseProductRequest(
    val productId: UUID,
    @field:DecimalMin("0.01")
    val allocatedValue: BigDecimal,
)
