package com.diadsimulation.expenses.core.domain.model

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseCategory
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import java.math.BigDecimal
import java.time.LocalDate

data class ExpenseUpdate(
    val name: String? = null,
    val description: String? = null,
    val value: BigDecimal? = null,
    val category: ExpenseCategory? = null,
    val allocationType: ExpenseAllocationType? = null,
    val status: ExpenseStatus? = null,
    val quantity: Int? = null,
    val products: List<ExpenseProduct>? = null,
    val expenseDate: LocalDate? = null,
    val fieldsPresent: Set<String> = emptySet(),
) {
    fun has(field: String): Boolean = field in fieldsPresent
}
