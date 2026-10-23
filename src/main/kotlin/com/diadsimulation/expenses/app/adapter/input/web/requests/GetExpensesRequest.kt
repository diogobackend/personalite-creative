package com.diadsimulation.expenses.app.adapter.input.web.requests

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE
import org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class GetExpensesRequest(
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
    @field:DateTimeFormat(iso = DATE)
    val expenseDate: LocalDate? = null,
    @field:DateTimeFormat(iso = DATE)
    val expenseDateFrom: LocalDate? = null,
    @field:DateTimeFormat(iso = DATE)
    val expenseDateTo: LocalDate? = null,
    @field:DateTimeFormat(iso = DATE_TIME)
    val createdFrom: OffsetDateTime? = null,
    @field:DateTimeFormat(iso = DATE_TIME)
    val createdTo: OffsetDateTime? = null,
    @field:DateTimeFormat(iso = DATE_TIME)
    val updatedFrom: OffsetDateTime? = null,
    @field:DateTimeFormat(iso = DATE_TIME)
    val updatedTo: OffsetDateTime? = null,
    val page: Int = 0,
    val size: Int = 20,
    val sortBy: String = "expenseDate",
    val sortDirection: String = "DESC",
)
