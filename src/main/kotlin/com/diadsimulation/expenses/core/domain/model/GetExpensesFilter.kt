package com.diadsimulation.expenses.core.domain.model

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class GetExpensesFilter(
    val name: String? = null,
    val description: String? = null,
    val value: BigDecimal? = null,
    val minValue: BigDecimal? = null,
    val maxValue: BigDecimal? = null,
    val category: ExpenseCategory? = null,
    val allocationType: ExpenseAllocationType? = null,
    val status: ExpenseStatus? = null,
    val productId: UUID? = null,
    val quantity: Int? = null,
    val minQuantity: Int? = null,
    val maxQuantity: Int? = null,
    val expenseDate: LocalDate? = null,
    val expenseDateFrom: LocalDate? = null,
    val expenseDateTo: LocalDate? = null,
    val createdFrom: OffsetDateTime? = null,
    val createdTo: OffsetDateTime? = null,
    val updatedFrom: OffsetDateTime? = null,
    val updatedTo: OffsetDateTime? = null,
    val page: Int = 0,
    val size: Int = 20,
    val sortBy: String = "expenseDate",
    val sortDirection: String = "DESC",
)
