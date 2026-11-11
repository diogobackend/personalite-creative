package com.diadsimulation.expenses.core.domain.model

import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType
import com.diadsimulation.expenses.core.domain.enums.ExpenseAllocationType.GENERAL
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus
import com.diadsimulation.expenses.core.domain.enums.ExpenseStatus.PENDING
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.math.RoundingMode.HALF_UP
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.Collections.emptyList
import java.util.UUID

data class Expense(
    val id: UUID = UUID.randomUUID(),
    val inventoryId: UUID? = null,
    val name: String,
    val description: String? = null,
    val value: BigDecimal,
    val paidAmount: BigDecimal = ZERO,
    val category: String,
    val categoryId: UUID? = null,
    val allocationType: ExpenseAllocationType = GENERAL,
    val status: ExpenseStatus = PENDING,
    val quantity: Int? = null,
    val products: List<ExpenseProduct> = emptyList(),
    val expenseDate: LocalDate,
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),
) {
    init {
        require(value > ZERO) {
            "O valor da despesa deve ser maior que zero."
        }
        require(paidAmount >= ZERO) {
            "O valor pago não pode ser negativo."
        }
        require(paidAmount <= value) {
            "O valor pago não pode ultrapassar o valor da despesa."
        }
        require(paidAmount.scale() <= 2) {
            "O valor pago deve possuir no máximo duas casas decimais."
        }
    }

    val paidPercentage: BigDecimal
        get() =
            paidAmount
                .multiply(HUNDRED)
                .divide(value, 2, HALF_UP)

    val remainingAmount: BigDecimal
        get() = value.subtract(paidAmount)

    private companion object {
        val HUNDRED = BigDecimal("100")
    }
}
