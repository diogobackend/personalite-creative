package com.diadsimulation.expenses.app.adapter.input.web.responses

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

data class ExpenseResponse(
    val id: UUID,
    val inventoryId: UUID? = null,
    val name: String,
    val description: String?,
    val value: BigDecimal,
    val category: ExpenseCategory,
    val allocationType: ExpenseAllocationType,
    val status: ExpenseStatus,
    val quantity: Int?,
    val products: List<ExpenseProductResponse>,
    val expenseDate: LocalDate,
    val createdAt: OffsetDateTime,
    val updatedAt: OffsetDateTime,
)
