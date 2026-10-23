package com.diadsimulation.expenses.core.domain.model

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.GENERAL
import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PENDING
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.Collections.emptyList
import java.util.UUID

data class Expense(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val description: String? = null,
    val value: BigDecimal,
    val category: ExpenseCategory,
    val allocationType: ExpenseAllocationType = GENERAL,
    val status: ExpenseStatus = PENDING,
    val quantity: Int? = null,
    val products: List<ExpenseProduct> = emptyList(),
    val expenseDate: LocalDate,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
)
