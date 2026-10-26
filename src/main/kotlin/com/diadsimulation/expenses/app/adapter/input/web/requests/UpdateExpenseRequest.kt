package com.diadsimulation.expenses.app.adapter.input.web.requests

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class UpdateExpenseRequest(
    @field:Size(min = 1, max = 150)
    val name: String? = null,
    val description: String? = null,
    @field:DecimalMin("0.01")
    val value: BigDecimal? = null,
    val category: ExpenseCategory? = null,
    val allocationType: ExpenseAllocationType? = null,
    val status: ExpenseStatus? = null,
    val quantity: Int? = null,
    @field:Valid
    val products: List<ExpenseProductRequest>? = null,
    val expenseDate: LocalDate? = null,
)
